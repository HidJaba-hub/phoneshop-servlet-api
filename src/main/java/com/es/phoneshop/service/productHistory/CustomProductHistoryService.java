package com.es.phoneshop.service.productHistory;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.ProductHistory;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Deque;

public class CustomProductHistoryService implements ProductHistoryService {

    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = "viewedProducts";
    private static final int MAX_VIEWED_PRODUCTS = 4;

    private CustomProductHistoryService() {

    }

    public static CustomProductHistoryService getInstance() {
        return SingletonManager.INSTANCE.getSingleton();
    }

    @Override
    public ProductHistory getProductHistory(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        Object syncObject = SyncObjectPool.getSyncObject(sessionId);
        synchronized (syncObject) {
            return (ProductHistory) request.getSession().getAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE);
        }
    }

    @Override
    public void addViewedProduct(ProductHistory viewedProducts, Product product) {
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
        private static final CustomProductHistoryService singleton = new CustomProductHistoryService();

        public CustomProductHistoryService getSingleton() {
            return singleton;
        }
    }
}
