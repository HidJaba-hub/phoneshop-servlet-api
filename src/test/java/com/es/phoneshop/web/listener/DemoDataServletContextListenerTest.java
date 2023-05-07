package com.es.phoneshop.web.listener;

import com.es.phoneshop.DAO.CustomProductDao;
import com.es.phoneshop.DAO.ProductDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {
    ProductDao productDao=CustomProductDao.getInstance();
    DemoDataServletContextListener demoDataServletContextListener=new DemoDataServletContextListener();
    @Mock
    ServletContext context;
    @Mock
    ServletContextEvent event;
    @Before
    public void setup() {
        when(event.getServletContext()).thenReturn(context);
        when(context.getInitParameter("insertDemoData")).thenReturn("true");
    }

    @Test
    public void initParameterTest() {//is it properly named?
        boolean insertDemoData = Boolean.valueOf(event.getServletContext().getInitParameter("insertDemoData"));

        assertTrue(insertDemoData);
    }

    @Test
    public void testContextInitialized() {
        demoDataServletContextListener.contextInitialized(event);

        assertNotNull(productDao.getProducts());
        assertTrue(productDao.getProducts().size()>0);
    }
}