package com.es.phoneshop.service;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.RecentlyViewedProducts;
import com.es.phoneshop.service.productHistory.CustomRecentlyViewedProductsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Deque;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomRecentlyViewedProductsTest {

    @Mock
    private RecentlyViewedProducts recentlyViewedProducts;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Deque<Product> productDeque;
    @InjectMocks
    private CustomRecentlyViewedProductsService productHistoryService;

    @Before
    public void setup() {
        productHistoryService = CustomRecentlyViewedProductsService.getInstance();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenRequest_whenGetProductHistory_thenVerifyProductHistory() {
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(any())).thenReturn(recentlyViewedProducts);

        RecentlyViewedProducts testRecentlyViewedProducts = productHistoryService.getRecentlyViewedProducts(request);

        assertEquals(recentlyViewedProducts, testRecentlyViewedProducts);
    }

    @Test
    public void givenProductsDeque_whenAddViewedProduct_thenVerifyAddedProduct() {
        Product product = new Product();
        when(recentlyViewedProducts.getRecentlyViewedProducts()).thenReturn(productDeque);

        productHistoryService.addRecentlyViewedProduct(recentlyViewedProducts, product);

        verify(productDeque).addFirst(product);
    }

    @Test
    public void givenMaxProductsDeque_whenAddViewedProduct_thenVerifyRemoveProduct() {
        Product product = new Product();
        when(productDeque.size()).thenReturn(4);
        when(recentlyViewedProducts.getRecentlyViewedProducts()).thenReturn(productDeque);

        productHistoryService.addRecentlyViewedProduct(recentlyViewedProducts, product);

        verify(productDeque).removeLast();
        verify(productDeque).addFirst(product);
    }

    @Test
    public void givenProductInDeque_whenAddViewedProduct_thenVerifyRemoveProduct() {
        Product product = new Product();
        when(productDeque.contains(product)).thenReturn(true);
        when(recentlyViewedProducts.getRecentlyViewedProducts()).thenReturn(productDeque);

        productHistoryService.addRecentlyViewedProduct(recentlyViewedProducts, product);

        verify(productDeque).remove(product);
        verify(productDeque).addFirst(product);
    }
}
