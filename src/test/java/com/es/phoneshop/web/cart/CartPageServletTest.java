package com.es.phoneshop.web.cart;

import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.utils.QuantityParseValidator;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartPageServletTest {

    @InjectMocks
    private final CartPageServlet servlet = new CartPageServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private DefaultCartService cartService;
    @Mock
    private QuantityParseValidator quantityParseValidator;

    @Before
    public void setup() throws ServletException {
        MockitoAnnotations.initMocks(this);
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenValidProduct_whenDoPost_thenSendSuccessRedirect() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
    }

    @Test
    public void givenEmptyProductIds_whenDoPost_thenDoPost() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(null);
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenInvalidQuantity_whenDoPost_thenSendErrorRedirect() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"a"});
        when(quantityParseValidator.validate(eq("a"), anyMap(), anyLong())).thenReturn(true);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenNegativeQuantity_whenDoPost_thenSendErrorRedirect() throws ServletException, IOException {
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"-1"});
        when(quantityParseValidator.validate(eq("-1"), anyMap(), anyLong())).thenReturn(true);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }
}
