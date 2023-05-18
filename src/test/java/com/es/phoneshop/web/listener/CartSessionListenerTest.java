package com.es.phoneshop.web.listener;

import com.es.phoneshop.model.entity.cart.Cart;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartSessionListenerTest {

    private static final String CART_SESSION_ATTRIBUTE = "cart";
    @Mock
    private HttpSessionEvent sessionEvent;
    @Mock
    private HttpSession session;
    @Mock
    private ServletContext context;
    @Mock
    private Cart cart;
    @InjectMocks
    private CartSessionListener cartSessionListener = new CartSessionListener();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(session.getServletContext()).thenReturn(context);
    }

    @Test
    public void givenTrueInitParameter_whenSessionCreated_thenVerifySession() {
        when(context.getInitParameter("insertCart")).thenReturn("true");
        when(sessionEvent.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("testSession");

        cartSessionListener.sessionCreated(sessionEvent);

        verify(session).setAttribute(eq(CART_SESSION_ATTRIBUTE), any());
    }

    @Test
    public void givenFalseInitParameter_whenSessionCreated_thenVerifyNoInteractions() {
        when(context.getInitParameter("insertCart")).thenReturn("false");
        when(sessionEvent.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("testSession");

        cartSessionListener.sessionCreated(sessionEvent);

        verify(session, never()).setAttribute(any(), any());
    }
}
