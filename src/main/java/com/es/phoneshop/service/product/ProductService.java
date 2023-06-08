package com.es.phoneshop.service.product;

import com.es.phoneshop.enums.SearchCriteria;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product getProductById(Long id) throws ProductNotFoundException;

    List<Product> getProducts();

    List<Product> getProductsByQuery(String query);

    List<Product> getProductsWithSortingAndQuery(SortField sortField, SortOrder sortOrder, String query);

    void deleteProduct(Long id);

    void saveProduct(Product product);

    void recalculateProductStock(Product product, int quantity);

    List<Product> getProductsByQuery(String description, BigDecimal minPrice, BigDecimal maxPrice, SearchCriteria searchCriteria);
}
