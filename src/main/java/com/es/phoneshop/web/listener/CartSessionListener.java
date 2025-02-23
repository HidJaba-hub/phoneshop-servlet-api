package com.es.phoneshop.web.listener;

import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.Currency;

public class CartSessionListener implements HttpSessionListener {

    private static final String CART_SESSION_ATTRIBUTE = "cart";
    private final Currency usd = Currency.getInstance("USD");

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSessionListener.super.sessionCreated(sessionEvent);
        HttpSession session = sessionEvent.getSession();

        boolean insertCart = Boolean.valueOf(session.getServletContext().getInitParameter("insertCart"));

        if (insertCart) {
            Cart cart = new Cart();
            cart.setCurrency(usd);
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        String sessionId = sessionEvent.getSession().getId();
        SyncObjectPool.cleanPool(sessionId);
        HttpSessionListener.super.sessionDestroyed(sessionEvent);
    }

}
