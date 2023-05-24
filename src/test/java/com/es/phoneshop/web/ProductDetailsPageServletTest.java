package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.RecentlyViewedProducts;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.service.productHistory.RecentlyViewedProductsService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    @InjectMocks
    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductService productService;
    @Mock
    private DefaultCartService cartService;
    @Mock
    private RecentlyViewedProductsService recentlyViewedProductsService;
    @Mock
    private ServletConfig config;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenProduct_whenGetRequest_thenGetException() throws ServletException, IOException {
        long productId = -1L;
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(productService.getProductById(productId)).thenThrow(new ProductNotFoundException(productId, "not found"));

        servlet.doGet(request, response);
    }

    @Test
    public void givenProduct_whenGetRequest_thenSetProductToAttribute() throws ServletException, IOException {
        Product product = new Product();
        RecentlyViewedProducts recentlyViewedProducts = new RecentlyViewedProducts();
        long productId = product.getId();
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(productService.getProductById(productId)).thenReturn(product);
        when(recentlyViewedProductsService.getRecentlyViewedProducts(request)).thenReturn(recentlyViewedProducts);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
    }

    @Test
    public void givenProductId_whenDoPost_thenVerifySendErrorRedirect() throws ServletException, IOException {
        long productId = -1;
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getParameter("quantity")).thenReturn("a");

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + "Not a number");
    }

    @Test
    public void givenValidQuantity_whenDoPost_thenVerifySendSuccessRedirect() throws ServletException, IOException, OutOfStockException {
        long productId = -1;
        int quantity = 1;
        Cart cart = new Cart();
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doPost(request, response);

        verify(cartService).addCartItem(cart, productId, quantity);
        verify(response).sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    @Test
    public void givenNegativeQuantity_whenDoPost_thenVerifySendErrorRedirect() throws IOException, OutOfStockException {
        long productId = -1;
        int quantity = -1;
        String errorString = "Wrong amount of products";
        Cart cart = mock(Cart.class);
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartService.getCart(request)).thenReturn(cart);
        doThrow(new IllegalArgumentException()).when(cartService).addCartItem(cart, productId, quantity);

        servlet.doPost(request, response);

        verify(cartService).addCartItem(cart, productId, -1);
        verify(response).sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + errorString
                + "&errorQuantity=" + quantity);
    }

    @Test
    public void givenExcessQuantity_whenDoPost_thenVerifySendErrorRedirect() throws ServletException, IOException, OutOfStockException {
        long productId = -1;
        int quantity = 100;
        int availableQuantity = 10;
        Cart cart = mock(Cart.class);
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartService.getCart(request)).thenReturn(cart);
        doThrow(new OutOfStockException(new Product(), quantity, availableQuantity))
                .when(cartService).addCartItem(cart, productId, quantity);

        servlet.doPost(request, response);

        verify(cartService).addCartItem(cart, productId, quantity);
        verify(response).sendRedirect(request.getContextPath() + "/products/" + productId +
                "?error=" + "Out of stock, available " + availableQuantity + "&errorQuantity=" + quantity);
    }

    @Test
    public void givenInvalidPathInfo_whenDoGet_thenSend404Error() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("");

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
    }

    @Test
    public void givenInvalidPathInfo_whenDoPost_thenSend404Error() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("");

        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
    }
}