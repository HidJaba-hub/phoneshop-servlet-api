package com.es.phoneshop.web;

import com.es.phoneshop.DAO.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.service.CustomProductService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    @InjectMocks
    private CustomProductService productService;
    @Mock
    private ProductDao productDao;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        productService = CustomProductService.getInstance();
        MockitoAnnotations.initMocks(this);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenProduct_whenGetRequest_thenGetException() throws ServletException, IOException {
        long productId = -1L;
        when(request.getPathInfo()).thenReturn("/" + productId);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
    }

    @Test
    public void givenProduct_whenGetRequest_thenSetProductToAttribute() throws ServletException, IOException {
        Product product = new Product();
        long productId = product.getId();
        when(request.getPathInfo()).thenReturn("/" + productId);

        when(productDao.getProductById(productId)).thenReturn(Optional.of(product));

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
    }
}