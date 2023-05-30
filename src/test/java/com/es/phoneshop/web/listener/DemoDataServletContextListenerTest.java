package com.es.phoneshop.web.listener;

import com.es.phoneshop.service.product.CustomProductService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {

    @InjectMocks
    private DemoDataServletContextListener demoDataServletContextListener = new DemoDataServletContextListener();
    @Mock
    private ServletContext context;
    @Mock
    private CustomProductService productServiceMock;
    @Mock
    private ServletContextEvent event;

    @Before
    public void setup() {
        when(event.getServletContext()).thenReturn(context);
    }

    @Test
    public void givenFalseInsertDataParameter_whenContextInitialized_thenDoNotInsertData() {
        when(context.getInitParameter("insertDemoData")).thenReturn("false");

        demoDataServletContextListener.contextInitialized(event);

        verifyNoInteractions(productServiceMock);
    }

    @Test
    public void givenInsertDemoDataTrue_whenContextInitialized_thenInsertData() {
        MockedStatic<CustomProductService> productServiceMockedStatic = mockStatic(CustomProductService.class);
        productServiceMockedStatic.when(CustomProductService::getInstance).thenReturn(productServiceMock);
        when(context.getInitParameter("insertDemoData")).thenReturn("true");

        demoDataServletContextListener.contextInitialized(event);

        verify(productServiceMock, atLeast(1)).saveProduct(any());
    }
}