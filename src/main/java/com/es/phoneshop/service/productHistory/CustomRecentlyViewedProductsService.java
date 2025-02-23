package com.es.phoneshop.service.productHistory;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.RecentlyViewedProducts;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Deque;

public class CustomRecentlyViewedProductsService implements RecentlyViewedProductsService {

    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = "viewedProducts";
    private static final int MAX_VIEWED_PRODUCTS = 4;

    private CustomRecentlyViewedProductsService() {

    }

    public static CustomRecentlyViewedProductsService getInstance() {
        return SingletonManager.INSTANCE.getSingleton();
    }

    @Override
    public RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object syncObject = SyncObjectPool.getSyncObject(session.getId());
        synchronized (syncObject) {
            return (RecentlyViewedProducts) session.getAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE);
        }
    }

    @Override
    public void addRecentlyViewedProduct(RecentlyViewedProducts viewedProducts, Product product) {
        Object syncObject = SyncObjectPool.getSyncObject(viewedProducts.getId().toString());
        synchronized (syncObject) {
            Deque<Product> viewedProductsDeque = viewedProducts.getRecentlyViewedProducts();
            if (findProductInDeque(viewedProductsDeque, product)) {
                viewedProductsDeque.remove(product);
            }
            if (viewedProductsDeque.size() == MAX_VIEWED_PRODUCTS) {
                viewedProductsDeque.removeLast();
            }
            viewedProductsDeque.addFirst(product);
        }
    }

    private boolean findProductInDeque(Deque<Product> viewedProductsDeque, Product product) {
        return viewedProductsDeque.contains(product);
    }

    private enum SingletonManager {

        INSTANCE;
        private static final CustomRecentlyViewedProductsService singleton = new CustomRecentlyViewedProductsService();

        public CustomRecentlyViewedProductsService getSingleton() {
            return singleton;
        }
    }
}
