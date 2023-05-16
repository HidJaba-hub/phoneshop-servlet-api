package com.es.phoneshop.web.listener;

import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class CartSessionListener implements HttpSessionListener {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + "cart";
    private HttpSession session;

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSessionListener.super.sessionCreated(sessionEvent);
        session = sessionEvent.getSession();

        boolean insertCart = Boolean.valueOf(session.getServletContext().getInitParameter("insertCart"));

        if (insertCart) {
            Cart cart = new Cart();
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        String sessionId = session.getId();
        SyncObjectPool.cleanPool(sessionId);
        HttpSessionListener.super.sessionDestroyed(sessionEvent);
    }

}
