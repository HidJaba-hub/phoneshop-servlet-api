package com.es.phoneshop.web.listener;

import com.es.phoneshop.service.cart.DefaultCartService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartSessionListenerTest {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + "cart";
    @Mock
    private HttpSessionEvent sessionEvent;
    @Mock
    private HttpSession session;
    @InjectMocks
    private CartSessionListener cartSessionListener = new CartSessionListener();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenSessionEvent_whenSessionCreated_thenVerifySession() {
        when(sessionEvent.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("testSession");

        cartSessionListener.sessionCreated(sessionEvent);

        verify(session).setAttribute(eq(CART_SESSION_ATTRIBUTE), any());
    }
}
