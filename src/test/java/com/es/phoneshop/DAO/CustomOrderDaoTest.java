package com.es.phoneshop.DAO;

import com.es.phoneshop.DAO.orderDao.CustomOrderDao;
import com.es.phoneshop.model.entity.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CustomOrderDaoTest {

    @InjectMocks
    private CustomOrderDao orderDao = CustomOrderDao.getInstance();
    @Mock
    private Order mockedOrder;
    @Mock
    private Order anotherMockedOrder;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        initializeMocks();
    }

    public void initializeMocks() {
        List<Order> mockedOrders = Arrays.asList(mockedOrder, anotherMockedOrder);
        orderDao.setItems(mockedOrders);
    }

    @Test
    public void givenValidOrder_whenSaveOrder_thenVerifyOrder() {
        orderDao.save(mockedOrder);
        List<Order> orders = orderDao.getItems();

        assertNotNull(orders);
        assertTrue(orders.contains(mockedOrder));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidOrder_whenSaveOrder_thenGetException() {
        orderDao.save(null);
    }

    @Test
    public void givenValidSecureId_whenGetOrderById_thenVerifyId() {
        when(mockedOrder.getSecureId()).thenReturn("1");

        Optional<Order> expectedOrder = orderDao.getOrderBySecureId("1");

        assertTrue(expectedOrder.isPresent());
        assertEquals(expectedOrder.get(), mockedOrder);
    }

    @Test
    public void givenInvalidSecureId_whenGetOrderById_thenVerifyNull() {
        when(mockedOrder.getSecureId()).thenReturn("1");

        Optional<Order> expectedOrder = orderDao.getOrderBySecureId("-1");

        assertFalse(expectedOrder.isPresent());
    }
}
