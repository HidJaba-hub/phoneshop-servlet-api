package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.model.entity.cart.CartItem;
import com.es.phoneshop.service.cart.DefaultCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomCartServiceTest {

    @Mock
    private Cart cart;
    @Mock
    private CartItem cartItem;
    @Mock
    private ProductService productService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Product product;
    private LinkedHashSet<CartItem> cartItems;
    @InjectMocks
    private DefaultCartService defaultCartService = DefaultCartService.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        initializeMocks();
    }

    private void initializeMocks() {
        when(cartItem.getProduct()).thenReturn(product);
        when(cartItem.getQuantity()).thenReturn(10);
        cartItems = new LinkedHashSet<>();
        cartItems.add(cartItem);
        when(cart.getItems()).thenReturn(cartItems);
    }

    @Test
    public void givenRequest_whenGetCart_thenVerifyCart() {
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(any())).thenReturn(cart);

        Cart testCart = defaultCartService.getCart(request);

        assertEquals(testCart, cart);
    }

    @Test
    public void givenNotEmptyCart_whenAddProduct_thenChangeQuantity() throws OutOfStockException {
        long productId = product.getId();
        int quantity = 10;
        when(product.getStock()).thenReturn(100);
        when(productService.getProductById(productId)).thenReturn(product);

        defaultCartService.addProductToCart(cart, productId, quantity);

        verify(cartItem).setQuantity(quantity + cartItem.getQuantity());
    }

    @Test
    public void givenNotEmptyCart_whenAddProduct_thenAddProduct() throws OutOfStockException {
        long productId = product.getId();
        int quantity = 10;
        cartItems.remove(cartItem);
        when(product.getStock()).thenReturn(100);
        when(productService.getProductById(productId)).thenReturn(product);

        defaultCartService.addProductToCart(cart, productId, quantity);

        verify(cart, atLeast(2)).getItems();
    }

    @Test(expected = OutOfStockException.class)
    public void givenOutOfStockQuantity_whenAddProduct_thenGetException() throws OutOfStockException {
        long productId = product.getId();
        int quantity = 100;
        when(product.getStock()).thenReturn(10);
        when(productService.getProductById(productId)).thenReturn(product);

        defaultCartService.addProductToCart(cart, productId, quantity);
    }

    @Test(expected = OutOfStockException.class)
    public void givenNegativeQuantity_whenAddProduct_thenGetException() throws OutOfStockException {
        long productId = product.getId();
        int quantity = -1;
        when(product.getStock()).thenReturn(10);
        when(productService.getProductById(productId)).thenReturn(product);

        defaultCartService.addProductToCart(cart, productId, quantity);
    }
}
