package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.model.entity.cart.CartItem;
import com.es.phoneshop.service.product.CustomProductService;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.utils.ReferenceTool;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Optional;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = "cart";
    private ProductService productService;

    private DefaultCartService() {
        productService = CustomProductService.getInstance();
    }

    public static DefaultCartService getInstance() {
        return SingletonManager.INSTANCE.getSingleton();
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object syncObject = SyncObjectPool.getSyncObject(session.getId());
        synchronized (syncObject) {
            return (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        }
    }

    @Override
    public void addCartItem(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Object syncObject = SyncObjectPool.getSyncObject(cart.getId().toString());
        synchronized (syncObject) {
            Product product = productService.getProductById(productId);
            Optional<CartItem> optionalCartItem = findCartItem(cart, product);
            int cartQuantity = optionalCartItem.map(CartItem::getQuantity).orElse(0);
            checkQuantity(product, quantity, cartQuantity);

            if (optionalCartItem.isPresent()) {
                optionalCartItem.get().setQuantity(cartQuantity + quantity);
            } else {
                cart.getItems().add(new CartItem(product, quantity));
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void updateCartItem(Cart cart, Long productId, int quantity, ReferenceTool<Integer> sameQuantityCount) throws OutOfStockException, ProductNotFoundException {
        Object syncObject = SyncObjectPool.getSyncObject(cart.getId().toString());
        synchronized (syncObject) {
            Product product = productService.getProductById(productId);
            Optional<CartItem> optionalCartItem = findCartItem(cart, product);
            checkQuantity(product, quantity);

            if (optionalCartItem.isEmpty()) {
                throw new ProductNotFoundException(productId, "Product wasn't found in cart");
            }
            if (optionalCartItem.get().getQuantity() != quantity) {
                optionalCartItem.get().setQuantity(quantity);
                recalculateCart(cart);
            } else {
                sameQuantityCount.set(sameQuantityCount.get() + 1);
            }
        }
    }

    @Override
    public void deleteCartItem(Cart cart, Long productId) throws ProductNotFoundException {
        Object syncObject = SyncObjectPool.getSyncObject(cart.getId().toString());
        synchronized (syncObject) {
            if (cart.getItems().removeIf(cartItem -> productId.equals(cartItem.getProduct().getId()))) {
                recalculateCart(cart);
            } else {
                throw new ProductNotFoundException(productId, "Product wasn't found in cart");
            }
        }
    }

    @Override
    public void cleanCart(Cart cart) {
        Object syncObject = SyncObjectPool.getSyncObject(cart.getId().toString());
        synchronized (syncObject) {
            cart.getItems().forEach(cartItem ->
                    productService.recalculateProductStock(cartItem.getProduct(), cartItem.getQuantity())
            );
            cart.getItems().clear();
            recalculateCart(cart);
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(Integer::intValue)
                .sum());

        cart.setTotalPrice(cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private Optional<CartItem> findCartItem(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(item -> product.getId().equals(item.getProduct().getId()))
                .findAny();
    }

    private void checkQuantity(Product product, int quantity, int cartQuantity) throws OutOfStockException {
        if (product.getStock() < quantity + cartQuantity) {
            throw new OutOfStockException(product, quantity, product.getStock() - cartQuantity);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private void checkQuantity(Product product, int quantity) throws OutOfStockException {
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private enum SingletonManager {
        INSTANCE;
        private static final DefaultCartService singleton = new DefaultCartService();

        public DefaultCartService getSingleton() {
            return singleton;
        }
    }
}
