package com.es.phoneshop.web;

import com.es.phoneshop.model.entity.Order;
import com.es.phoneshop.service.order.OrderService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderOverviewPageServletTest {
    @InjectMocks
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private OrderService orderService;
    @Mock
    private ServletConfig config;
    @Mock
    private Order order;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        MockitoAnnotations.initMocks(this);
        when(request.getLocale()).thenReturn(Locale.US);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenOrderId_whenDoGet_thenSetAttribute() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/" + 1);

        servlet.doGet(request, response);

        request.setAttribute("order", orderService.getOrderById(String.valueOf(1)));
        request.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(request, response);
    }

    @Test
    public void givenInvalidPathInfo_whenDoPost_thenSend404Error() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("");

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
    }
}
