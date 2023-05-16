package com.es.phoneshop.service;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.ProductHistory;
import com.es.phoneshop.service.productHistory.CustomProductHistoryService;
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

public class CustomProductHistoryTest {

    @Mock
    private ProductHistory productHistory;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Deque<Product> productDeque;
    @InjectMocks
    private CustomProductHistoryService productHistoryService;

    @Before
    public void setup() {
        productHistoryService = CustomProductHistoryService.getInstance();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenRequest_whenGetProductHistory_thenVerifyProductHistory() {
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(any())).thenReturn(productHistory);

        ProductHistory testProductHistory = productHistoryService.getProductHistory(request);

        assertEquals(testProductHistory, productHistory);
    }

    @Test
    public void givenProductsDeque_whenAddViewedProduct_thenVerifyAddedProduct() {
        Product product = new Product();
        when(productHistory.getRecentlyViewedProducts()).thenReturn(productDeque);

        productHistoryService.addViewedProduct(productHistory, product);

        verify(productDeque).addFirst(product);
    }

    @Test
    public void givenMaxProductsDeque_whenAddViewedProduct_thenVerifyRemoveProduct() {
        Product product = new Product();
        when(productDeque.size()).thenReturn(4);
        when(productHistory.getRecentlyViewedProducts()).thenReturn(productDeque);

        productHistoryService.addViewedProduct(productHistory, product);

        verify(productDeque).removeLast();
        verify(productDeque).addFirst(product);
    }

    @Test
    public void givenProductInDeque_whenAddViewedProduct_thenVerifyRemoveProduct() {
        Product product = new Product();
        when(productDeque.contains(product)).thenReturn(true);
        when(productHistory.getRecentlyViewedProducts()).thenReturn(productDeque);

        productHistoryService.addViewedProduct(productHistory, product);

        verify(productDeque).remove(product);
        verify(productDeque).addFirst(product);
    }
}
