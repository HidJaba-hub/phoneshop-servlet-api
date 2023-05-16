package com.es.phoneshop.web.listener;

import com.es.phoneshop.model.entity.ProductHistory;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class ProductHistorySessionListener implements HttpSessionListener {

    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = "viewedProducts";
    private HttpSession session;

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSessionListener.super.sessionCreated(sessionEvent);
        session = sessionEvent.getSession();

        boolean insertCart = Boolean.valueOf(session.getServletContext().getInitParameter("insertProductHistory"));

        if (insertCart) {
            ProductHistory productHistory = new ProductHistory();
            session.setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, productHistory);
        }
        HttpSessionListener.super.sessionCreated(sessionEvent);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        String sessionId = session.getId();
        SyncObjectPool.cleanPool(sessionId);
        HttpSessionListener.super.sessionDestroyed(sessionEvent);
    }
}
