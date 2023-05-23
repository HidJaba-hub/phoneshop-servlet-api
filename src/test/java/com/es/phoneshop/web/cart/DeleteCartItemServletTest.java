package com.es.phoneshop.web.cart;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.DefaultCartService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeleteCartItemServletTest {

    @InjectMocks
    private final DeleteCartItemServlet servlet = new DeleteCartItemServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private DefaultCartService cartService;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Cart cart;
    @Mock
    private ServletConfig config;


    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenProduct_whenDeleteProduct_thenSendSuccessRedirect() throws IOException {
        long productId = -1L;
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }

    @Test
    public void givenInvalidProduct_whenDeleteProduct_thenSendErrorRedirect() throws IOException {
        long productId = -1L;
        String errorString = "Product not found";
        when(request.getPathInfo()).thenReturn("/" + productId);
        doThrow(new ProductNotFoundException(productId, errorString)).when(cartService).deleteCartItem(any(), any());

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart?errors=" + errorString);
    }
}
