package com.es.phoneshop.web;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.service.ProductService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

    @Before
    public void setup() {
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
        long productId = product.getId();
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(productService.getProductById(productId)).thenReturn(product);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
    }
}