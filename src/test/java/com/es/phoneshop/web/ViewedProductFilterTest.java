package com.es.phoneshop.web;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.productHistory.ProductHistoryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViewedProductFilterTest {

    @InjectMocks
    private ViewedProductFilter viewedProductFilter = new ViewedProductFilter();
    @Mock
    private ProductHistoryService productHistoryService;
    @Mock
    private ProductService productService;
    @Mock
    private ServletRequest servletRequest;
    @Mock
    private ServletResponse servletResponse;
    @Mock
    private FilterConfig config;

    @Before
    public void setup() {
        viewedProductFilter.init(config);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenViewedProducts_whenDoFilter_thenVerifyFilterChain() throws ServletException, IOException {
        long productId = 1;
        FilterChain filterChain = mock(FilterChain.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(productService.getProductById(productId)).thenReturn(new Product());

        viewedProductFilter.doFilter(request, servletResponse, filterChain);

        verify(productHistoryService, atLeast(2)).getProductHistory(any());
        verify(request).setAttribute(eq("viewedProducts"), any());
        verify(productHistoryService).addViewedProduct(any(), any());
        verify(filterChain).doFilter(request, servletResponse);
    }
}
