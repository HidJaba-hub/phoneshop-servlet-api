package com.es.phoneshop.service.productHistory;

import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.model.entity.ProductHistory;
import jakarta.servlet.http.HttpServletRequest;

public interface ProductHistoryService {

    ProductHistory getProductHistory(HttpServletRequest request);

    void addViewedProduct(ProductHistory viewedProducts, Product product);
}
