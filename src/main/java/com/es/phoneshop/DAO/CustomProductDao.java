package com.es.phoneshop.DAO;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.StringChecker;
import com.es.phoneshop.exception.ProductDefinitionException;
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
    private final Comparator<Product> defaultComparator = Comparator.comparing(Product::getDescription);


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
    public List<Product> findProducts(SortField sortField, SortOrder sortOrder, String query) {
        readLock.lock();
        try {
            Comparator<Product> comparator = setOrderComparator(sortOrder,
                                                setFieldComparator(sortField, query,
                                                        setQueryComparator(query, defaultComparator)));
            // i created this nested system of comparators that are independent of each other so it can be easily expanded
            // it is also safe because as a root we have default comparator, and if all parameters will be null it will just return default comparator
            return products.stream()
                    .filter(product -> StringUtils.isEmpty(query)
                            || StringChecker.calculateStringSimilarity(product.getDescription(), query) > 0)
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getPrice().compareTo(BigDecimal.valueOf(0)) > 0)
                    .filter(product -> product.getStock() > 0)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }

    }

    private Comparator<Product> setQueryComparator(String query, Comparator<Product> comparator) {
        if(StringUtils.isEmpty(query)) return comparator;
        return Comparator.comparing(Product::getDescription,
                        (s1, s2) -> Double.compare(
                                StringChecker.calculateStringSimilarity(s2, query),
                                StringChecker.calculateStringSimilarity(s1, query)
                        )
        );
    }
    private Comparator<Product> setFieldComparator(SortField sortField, String query, Comparator<Product> comparator) {
        if (sortField == null) return comparator;
        return switch (sortField){ // I decided to make a switch because it will be easier to expand in the future
            case PRICE -> Comparator.comparing(Product::getPrice);
            case DESCRIPTION  -> (StringUtils.isEmpty(query))
                                ? Comparator.comparing(Product::getDescription)
                                : comparator;
            // if we have filter on description we just return comparator with string filtering, so we can just change the order
            // but if we change price order for ex (there isn't any price filter, but it can be easily added)
            // it will change order according price, excluding filtering
        };
    }
    private Comparator<Product> setOrderComparator(SortOrder sortOrder, Comparator<Product> comparator){
        if (sortOrder == null) return comparator;
        return switch (sortOrder){
            case DESC -> comparator.reversed();
            case ASC  -> comparator;
        };
    }
    @Override
    public void save(Product product) throws ProductDefinitionException {
        if (product == null)
            throw new ProductDefinitionException("Product has no data");
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

    @Override
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void changeChosenState(Product product, boolean state) {
        product.setIsChosen(state);
    }
}
