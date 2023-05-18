package com.es.phoneshop.web.listener;

import com.es.phoneshop.model.entity.RecentlyViewedProducts;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class ProductHistorySessionListener implements HttpSessionListener {

    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = "viewedProducts";

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSessionListener.super.sessionCreated(sessionEvent);
        HttpSession session = sessionEvent.getSession();

        boolean insertCart = Boolean.valueOf(session.getServletContext().getInitParameter("insertProductHistory"));

        if (insertCart) {
            RecentlyViewedProducts recentlyViewedProducts = new RecentlyViewedProducts();
            session.setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, recentlyViewedProducts);
        }
        HttpSessionListener.super.sessionCreated(sessionEvent);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        String sessionId = sessionEvent.getSession().getId();
        SyncObjectPool.cleanPool(sessionId);
        HttpSessionListener.super.sessionDestroyed(sessionEvent);
    }
}
