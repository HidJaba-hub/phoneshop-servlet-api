package com.es.phoneshop.web.listener;

import com.es.phoneshop.DAO.CustomProductDao;
import com.es.phoneshop.DAO.ProductDao;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.web.listener.DemoDataServletContextListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {

    private CustomProductService productService=CustomProductService.getInstance();
    private DemoDataServletContextListener demoDataServletContextListener = new DemoDataServletContextListener();
    @Mock
    private ServletContext context;
    @Mock
    private ProductService productServiceMock;
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

        verifyZeroInteractions(productServiceMock);
    }

    @Test
    public void givenInsertDemoDataTrue_whenContextInitialized_thenInsertData() {
        when(context.getInitParameter("insertDemoData")).thenReturn("true");
        demoDataServletContextListener.contextInitialized(event);

        List<Product> products = productService.getProducts();

        assertNotNull(products);
    }
}