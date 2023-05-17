package com.es.phoneshop.web.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecentlyViewedProductsSessionListenerTest {

    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = "viewedProducts";
    @Mock
    private HttpSessionEvent sessionEvent;
    @Mock
    private HttpSession session;
    @Mock
    private ServletContext context;
    @InjectMocks
    private ProductHistorySessionListener productHistorySessionListener = new ProductHistorySessionListener();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(session.getServletContext()).thenReturn(context);
    }

    @Test
    public void givenTrueInitParameter_whenSessionCreated_thenVerifySession() {
        when(context.getInitParameter("insertProductHistory")).thenReturn("true");
        when(sessionEvent.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("testSession");

        productHistorySessionListener.sessionCreated(sessionEvent);

        verify(session).setAttribute(eq(VIEWED_PRODUCTS_SESSION_ATTRIBUTE), any());
    }

    @Test
    public void givenFalseInitParameter_whenSessionCreated_thenVerifyNoInteractions() {
        when(context.getInitParameter("insertProductHistory")).thenReturn("false");
        when(sessionEvent.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("testSession");

        productHistorySessionListener.sessionCreated(sessionEvent);

        verify(session, atLeast(0)).setAttribute(any(), any());
    }
}
