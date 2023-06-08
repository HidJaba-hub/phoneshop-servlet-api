package com.es.phoneshop.service.product;

import com.es.phoneshop.DAO.productDao.CustomProductDao;
import com.es.phoneshop.DAO.productDao.ProductDao;
import com.es.phoneshop.enums.SearchCriteria;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomProductService implements ProductService {

    private static CustomProductService customProductService;
    private ProductDao productDao;

    private CustomProductService() {
        productDao = CustomProductDao.getInstance();
    }

    public static CustomProductService getInstance() {
        if (customProductService == null) {
            synchronized (CustomProductService.class) {
                customProductService = new CustomProductService();
            }
        }
        return customProductService;
    }

    @Override
    public List<Product> getProductsWithSortingAndQuery(SortField sortField, SortOrder sortOrder, String query) {
        return productDao.sortProductsByFieldAndQuery(sortField, sortOrder, query);
    }

    @Override
    public List<Product> getProductsByQuery(String query) {
        return productDao.findProductsByQuery(query);
    }

    @Override
    public void deleteProduct(Long id) {
        productDao.delete(id);
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productDao.getProductById(id);
        return optionalProduct.orElseThrow(() ->
                new ProductNotFoundException(id, "Product not found for id: " + id));
    }

    @Override
    public List<Product> getProducts() {
        return productDao.findProducts();
    }

    @Override
    public void saveProduct(Product product) {
        productDao.save(product);
    }

    @Override
    public void recalculateProductStock(Product product, int quantity) {
        product.setStock(product.getStock() - quantity);
        productDao.save(product);
    }

    @Override
    public List<Product> getProductsByQuery(String description, BigDecimal minPrice,
                                            BigDecimal maxPrice, SearchCriteria searchCriteria) {
        List<Product> products = new ArrayList<>();
        switch (searchCriteria) {
            case ANY_WORDS -> products = productDao.findProductsByQuery(description);
            case ALL_WORDS -> products = productDao.findProductsByQueryAllMatch(description);
        }
        return productDao.findProductsInPriceRange(products, minPrice, maxPrice);
    }
}
