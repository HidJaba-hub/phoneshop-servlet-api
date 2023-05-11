package com.es.phoneshop.DAO;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.StringChecker;
import com.es.phoneshop.model.entity.Product;
import org.apache.maven.shared.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CustomProductDao implements ProductDao {

    private static CustomProductDao customProductDao;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private List<Product> products;


    private CustomProductDao() {
        products = new ArrayList<>();
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
        readLock.lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny();
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public List<Product> findProducts() {
        readLock.lock();
        try {
            return products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getPrice().compareTo(BigDecimal.valueOf(0)) > 0)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    public List<Product> findProductsByQuery(String query) {
        readLock.lock();
        try {
            Comparator<Product> comparator = setQueryComparator(query);
            return getProductList(query, comparator);
        } finally {
            readLock.unlock();
        }
    }

    private List<Product> getProductList(String query, Comparator<Product> comparator) {
        return products.stream()
                .filter(product -> StringUtils.isEmpty(query) ||
                        StringChecker.calculateStringSimilarity(product.getDescription(), query) > 0)
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getPrice().compareTo(BigDecimal.valueOf(0)) > 0)
                .filter(product -> product.getStock() > 0)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> sortProductsByFieldAndQuery(SortField sortField, SortOrder sortOrder, String query) {
        readLock.lock();
        try {
            Comparator<Product> comparator = setOrderComparator(sortOrder,
                    setFieldComparator(sortField, query,
                            setQueryComparator(query)));
            return getProductList(query, comparator);
        } finally {
            readLock.unlock();
        }

    }

    private Comparator<Product> setQueryComparator(String query) {
        if (StringUtils.isEmpty(query)) {
            return new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return 0;
                }
            };
        }
        return Comparator.comparing(Product::getDescription,
                (s1, s2) -> Double.compare(
                        StringChecker.calculateStringSimilarity(s2, query),
                        StringChecker.calculateStringSimilarity(s1, query)
                )
        );
    }

    private Comparator<Product> setFieldComparator(SortField sortField, String query, Comparator<Product> comparator) {
        return switch (sortField) {
            case PRICE -> Comparator.comparing(Product::getPrice);
            case DESCRIPTION -> (StringUtils.isEmpty(query))
                    ? Comparator.comparing(Product::getDescription)
                    : comparator;
        };
    }

    private Comparator<Product> setOrderComparator(SortOrder sortOrder, Comparator<Product> comparator) {
        return switch (sortOrder) {
            case DESC -> comparator.reversed();
            case ASC -> comparator;
        };
    }

    @Override
    public void save(Product product) throws IllegalArgumentException {
        if (product == null)
            throw new IllegalArgumentException("Product has no data");
        writeLock.lock();
        try {
            Optional<Product> oldProduct = getProductById(product.getId());
            if (oldProduct.isPresent()) {
                products.set(products.indexOf(oldProduct.get()), product);
            } else {
                products.add(product);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        writeLock.lock();
        try {
            products = products.stream()
                    .filter(product -> !id.equals(product.getId()))
                    .collect(Collectors.toList());
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
