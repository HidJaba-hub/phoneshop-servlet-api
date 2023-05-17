package com.es.phoneshop.service.productHistory;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.RecentlyViewedProducts;
import jakarta.servlet.http.HttpServletRequest;

public interface ProductHistoryService {

    RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request);

    void addRecentlyViewedProduct(RecentlyViewedProducts viewedProducts, Product product);
}
