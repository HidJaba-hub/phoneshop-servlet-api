package com.es.phoneshop.web.cart;

import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.DefaultCartService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MiniCartServletTest {

    @InjectMocks
    private final MiniCartServlet servlet = new MiniCartServlet();
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

    @Before
    public void setup() throws ServletException {
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenProduct_whenDeleteProduct_thenSendSuccessRedirect() throws IOException, ServletException {
        long productId = -1L;
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doGet(request, response);

        verify(requestDispatcher).include(request, response);
    }
}
