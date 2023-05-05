package com.es.phoneshop.web;

import com.es.phoneshop.exception.ProductDefinitionException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
    }

    @Test(expected = ProductDefinitionException.class)
    public void givenProduct_whenGetRequest_thenGetException() throws ServletException, IOException {
        long productId = -1L;
        when(request.getPathInfo()).thenReturn("/" + productId);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
    }
}