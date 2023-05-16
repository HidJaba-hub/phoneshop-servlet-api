package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.model.entity.cart.CartItem;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + "cart";
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
        }
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
