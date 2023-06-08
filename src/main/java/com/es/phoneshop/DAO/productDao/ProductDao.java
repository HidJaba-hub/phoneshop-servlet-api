package com.es.phoneshop.DAO.productDao;

import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductDao {

    Optional<Product> getProductById(Long id);

    List<Product> findProducts();

    List<Product> sortProductsByFieldAndQuery(SortField sortField, SortOrder sortOrder, String query);

    List<Product> findProductsByQuery(String query);

    List<Product> findProductsByQueryAllMatch(String query);

    void save(Product product) throws IllegalArgumentException;

    void delete(Long id);

    List<Product> getProducts();

    List<Product> findProductsInPriceRange(List<Product> products, BigDecimal minPrice, BigDecimal maxPrice);
}
