package com.es.phoneshop.web;

import com.es.phoneshop.service.ProductService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    private final ProductListPageServlet servlet = new ProductListPageServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private ProductService productService;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setProductService(productService);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenEmptyQuery_whenDoGet_thenVerifyGetProducts() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn("");

        servlet.doGet(request, response);

        verify(productService).getProducts();
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
    }

    @Test
    public void givenQuery_whenDoGet_thenVerifyFindProductsByQuery() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn("description");

        servlet.doGet(request, response);

        verify(productService).findProductsByQuery("description");
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
    }
}