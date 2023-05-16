package com.es.phoneshop.service.productHistory;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.ProductHistory;
import com.es.phoneshop.utils.SyncObjectPool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
        HttpSession session = request.getSession();
        session = (HttpSession) SyncObjectPool.getSyncObject(session.hashCode(), session);
        synchronized (session) {
            ProductHistory productHistory = (ProductHistory) session.getAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE);
            if (productHistory == null) {
                request.getSession().setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, productHistory = new ProductHistory());
            }
            return productHistory;
        }
    }

    @Override
    public void addViewedProduct(ProductHistory viewedProducts, Product product) {
        viewedProducts = (ProductHistory) SyncObjectPool.getSyncObject(viewedProducts.hashCode(), viewedProducts);
        synchronized (viewedProducts) {
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
