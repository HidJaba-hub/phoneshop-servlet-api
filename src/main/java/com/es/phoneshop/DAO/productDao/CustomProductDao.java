package com.es.phoneshop.DAO.productDao;

import com.es.phoneshop.DAO.GenericDao;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.utils.StringChecker;
import org.apache.maven.shared.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CustomProductDao extends GenericDao<Product> implements ProductDao {

    private static CustomProductDao customProductDao;


    private CustomProductDao() {
        super();
    }

    public static CustomProductDao getInstance() {
        if (customProductDao == null) {
            synchronized (CustomProductDao.class) {
                customProductDao = new CustomProductDao();
            }
        }
        return customProductDao;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return super.getItemById(id);
    }

    @Override
    public List<Product> findProducts() {
        getReadLock().lock();
        try {
            return getItems().stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getPrice().compareTo(BigDecimal.valueOf(0)) > 0)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        } finally {
            getReadLock().unlock();
        }
    }

    public List<Product> findProductsByQuery(String query) {
        getReadLock().lock();
        try {
            Comparator<Product> comparator = setQueryComparator(query);
            return getProductList(query, comparator);
        } finally {
            getReadLock().unlock();
        }
    }

    private List<Product> getProductList(String query, Comparator<Product> comparator) {
        return getItems().stream()
                .filter(product -> StringUtils.isEmpty(query) ||
                        StringChecker.calculateStringSimilarity(product.getDescription(), query) > 0)
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getPrice().compareTo(BigDecimal.valueOf(0)) > 0)
                .filter(product -> product.getStock() > 0)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findProductsByQueryAllMatch(String query) {
        getReadLock().lock();
        try {
            return getItems().stream()
                    .filter(product -> StringUtils.isEmpty(query) ||
                            StringChecker.calculateStringIdentity(product.getDescription(), query))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getPrice().compareTo(BigDecimal.valueOf(0)) > 0)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        } finally {
            getReadLock().unlock();
        }
    }

    @Override
    public List<Product> findProductsInPriceRange(List<Product> products, BigDecimal minPrice, BigDecimal maxPrice) {
        getReadLock().lock();
        try {
            return products.stream()
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        } finally {
            getReadLock().unlock();
        }
    }

    @Override
    public List<Product> sortProductsByFieldAndQuery(SortField sortField, SortOrder sortOrder, String query) {
        getReadLock().lock();
        try {
            Comparator<Product> comparator = setOrderComparator(sortOrder,
                    setFieldComparator(sortField, query));
            return getProductList(query, comparator);
        } finally {
            getReadLock().unlock();
        }

    }

    private Comparator<Product> setQueryComparator(String query) {
        return Comparator.comparing(Product::getDescription,
                (s1, s2) -> Double.compare(
                        StringChecker.calculateStringSimilarity(s2, query),
                        StringChecker.calculateStringSimilarity(s1, query)
                )
        );
    }

    private Comparator<Product> setFieldComparator(SortField sortField, String query) {
        return switch (sortField) {
            case PRICE -> Comparator.comparing(Product::getPrice);
            case DESCRIPTION -> (StringUtils.isEmpty(query))
                    ? Comparator.comparing(Product::getDescription)
                    : setQueryComparator(query);
        };
    }

    private Comparator<Product> setOrderComparator(SortOrder sortOrder, Comparator<Product> comparator) {
        return switch (sortOrder) {
            case DESC -> comparator.reversed();
            case ASC -> comparator;
        };
    }

    @Override
    public List<Product> getProducts() {
        return getItems();
    }

    public void setProducts(List<Product> products) {
        setItems(products);
    }
}
