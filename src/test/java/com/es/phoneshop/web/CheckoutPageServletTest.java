package com.es.phoneshop.web;

import com.es.phoneshop.model.entity.Order;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.order.OrderService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckoutPageServletTest {

    @InjectMocks
    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private OrderService orderService;
    @Mock
    private CartService cartService;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;
    @Mock
    private Cart cart;
    @Mock
    private Order order;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        MockitoAnnotations.initMocks(this);

        when(cartService.getCart(request)).thenReturn(cart);
        when(orderService.getOrder(cart)).thenReturn(order);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenOrder_whenDoGet_thenSetAttributes() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenValidData_whenDoPost_thenSetAttributes() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("TestName");
        when(request.getParameter("lastName")).thenReturn("TestName");
        when(request.getParameter("deliveryAddress")).thenReturn("Test Address");
        when(request.getParameter("phone")).thenReturn("+375293434802");
        when(request.getParameter("deliveryDate")).thenReturn("2024-05-27");
        when(request.getParameter("paymentMethod")).thenReturn("CACHE");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void givenNameWithNumbers_whenDoPost_thenSetAttributes() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("123");
        when(request.getParameter("lastName")).thenReturn("TestName");
        when(request.getParameter("deliveryAddress")).thenReturn("Test Address");
        when(request.getParameter("phone")).thenReturn("+375293434802");
        when(request.getParameter("deliveryDate")).thenReturn("2024-05-27");
        when(request.getParameter("paymentMethod")).thenReturn("CACHE");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenNullParams_whenDoPost_thenSetAttributes() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn(null);
        when(request.getParameter("lastName")).thenReturn(null);
        when(request.getParameter("deliveryAddress")).thenReturn(null);
        when(request.getParameter("phone")).thenReturn("");
        when(request.getParameter("deliveryDate")).thenReturn("");
        when(request.getParameter("paymentMethod")).thenReturn("");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenPastDate_whenDoPost_thenSetAttributes() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("TestName");
        when(request.getParameter("lastName")).thenReturn("TestName");
        when(request.getParameter("deliveryAddress")).thenReturn("Test Address");
        when(request.getParameter("phone")).thenReturn("+375293434802");
        when(request.getParameter("deliveryDate")).thenReturn("2022-04-26");
        when(request.getParameter("paymentMethod")).thenReturn("CACHE");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

}
