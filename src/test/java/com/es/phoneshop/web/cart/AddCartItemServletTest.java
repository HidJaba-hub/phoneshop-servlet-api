package com.es.phoneshop.web.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.entity.Product;
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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddCartItemServletTest {

    @InjectMocks
    private final AddCartItemServlet servlet = new AddCartItemServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private DefaultCartService cartService;
    @Mock
    private Product product;

    @Before
    public void setup() throws ServletException {
        MockitoAnnotations.initMocks(this);
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenValidProduct_whenDoPost_thenSendSuccessRedirect() throws IOException {
        long productId = 1L;
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});
        when(request.getParameter("path")).thenReturn("path");
        when(request.getPathInfo()).thenReturn("/" + productId);

        servlet.doPost(request, response);

        verify(response).sendRedirect("path" + "&message=Cart updated successfully");
    }

    @Test
    public void givenInvalidQuantity_whenDoPost_thenSendErrorRedirect() throws IOException {
        long productId = 1L;
        String quantity = "1a";
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{quantity});
        when(request.getParameter("path")).thenReturn("path");
        when(request.getPathInfo()).thenReturn("/" + productId);

        servlet.doPost(request, response);

        verify(response).sendRedirect("path" + "&errors=" + "Not a number" + "&id=" + productId + "&quantity=" + quantity);
    }

    @Test
    public void givenInvalidProduct_whenDoPost_thenSendErrorRedirect() throws IOException, OutOfStockException {
        long productId = 1L;
        String quantity = "1";
        int available = 1;
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{quantity});
        when(request.getParameter("path")).thenReturn("path");
        when(request.getPathInfo()).thenReturn("/" + productId);
        doThrow(new OutOfStockException(product, 10, available)).when(cartService).addProductToCart(any(), anyLong(), anyInt());

        servlet.doPost(request, response);

        verify(response).sendRedirect("path" + "&errors=" + "Out of stock, available " + available + "&id=" + productId + "&quantity=" + quantity);
    }
}
