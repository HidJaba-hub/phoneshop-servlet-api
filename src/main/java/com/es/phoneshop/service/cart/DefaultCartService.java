package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.exception.SameArgumentException;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.model.entity.cart.CartItem;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
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
    public void addProductToCart(Cart cart, Long productId, int quantity) throws OutOfStockException {
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
    public void updateProductInCart(Cart cart, Long productId, int quantity) throws OutOfStockException, ProductNotFoundException {
        Object syncObject = SyncObjectPool.getSyncObject(cart.getId().toString());
        synchronized (syncObject) {
            Product product = productService.getProductById(productId);
            Optional<CartItem> optionalCartItem = findCartItem(cart, product);
            checkQuantity(product, quantity, 0);

            if (optionalCartItem.isPresent()) {
                if (optionalCartItem.get().getQuantity() == quantity) {
                    throw new SameArgumentException(optionalCartItem.get());
                    //it is for resource economy: if the quantity is the same there is no sense to waste our resources
                } else {
                    optionalCartItem.get().setQuantity(quantity);
                    recalculateCart(cart);
                }
            } else {
                throw new ProductNotFoundException(productId, "Product wasn't found in cart");
            }
        }
    }

    @Override
    public void deleteProductInCart(Cart cart, Long productId) throws ProductNotFoundException {
        boolean isDeleted;
        Object syncObject = SyncObjectPool.getSyncObject(cart.getId().toString());
        synchronized (syncObject) {
            isDeleted = cart.getItems().removeIf(cartItem ->
                    productId.equals(cartItem.getProduct().getId()));
        }
        if (isDeleted) {
            recalculateCart(cart);
        } else {
            throw new ProductNotFoundException(productId, "Product wasn't found in cart");
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(cartItem -> cartItem)
                .sum());

        cart.setTotalPrice(BigDecimal.valueOf(cart.getItems().stream()
                .mapToInt(item -> item.getProduct().getPrice().intValueExact() * item.getQuantity())
                .sum()
        ));
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

    private enum SingletonManager {
        INSTANCE;
        private static final DefaultCartService singleton = new DefaultCartService();

        public DefaultCartService getSingleton() {
            return singleton;
        }
    }
}
