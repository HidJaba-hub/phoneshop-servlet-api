package com.es.phoneshop.service;

import com.es.phoneshop.DAO.orderDao.CustomOrderDao;
import com.es.phoneshop.PaymentMethod;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.entity.Order;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.order.CustomOrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomOrderServiceTest {

    @Mock
    private CustomOrderDao orderDao;
    @Mock
    private Cart cart;
    @InjectMocks
    private CustomOrderService orderService = CustomOrderService.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenCart_whenGetOrder_thenVerifyOrder() {
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(1));

        Order order = orderService.getOrder(cart);

        assertEquals(order.getItems(), cart.getItems());
        assertEquals(order.getCurrency(), cart.getCurrency());
        assertEquals(order.getTotalQuantity(), cart.getTotalQuantity());
        assertEquals(order.getSubtotal(), cart.getTotalPrice());
        assertNotNull(order);
    }

    @Test
    public void givenValidId_whenGetOrderById_thenVerifyOrderId() {
        Order order = new Order();
        when(orderDao.getOrderBySecureId("1")).thenReturn(Optional.of(order));

        Order expectedOrder = orderService.getOrderById("1");

        assertEquals(expectedOrder, order);
    }

    @Test(expected = OrderNotFoundException.class)
    public void givenInvalidId_whenGetOrderById_thenGetException() {
        orderService.getOrderById(null);
    }

    @Test
    public void givenCart_whenPlaceOrder_thenVerifyDao() {
        Order order = new Order();

        orderService.placeOrder(order);

        verify(orderDao).save(order);
    }

    @Test
    public void givenPaymentMethods_whenGetPaymentMethods_thenVerifyPaymentMethod() {
        List<PaymentMethod> paymentMethods = orderService.getPaymentMethods();

        assertNotNull(paymentMethods);
    }
}
