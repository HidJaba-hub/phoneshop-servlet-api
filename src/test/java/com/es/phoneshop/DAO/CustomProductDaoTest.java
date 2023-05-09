package com.es.phoneshop.DAO;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.model.entity.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomProductDaoTest {
    private ProductDao productDao;
    @Mock
    private Product mockedProduct;
    @Mock
    private Product anotherMockedProduct;

    @Before
    public void setup() {
        productDao = CustomProductDao.getInstance();
        initializeMocks();
    }

    public void initializeMocks() {
        List<Product> mockedProducts = Arrays.asList(mockedProduct, anotherMockedProduct);
        mockedProducts
                .forEach(product -> {
                    when(product.getDescription()).thenReturn("test");
                    when(product.getStock()).thenReturn(10);
                    when(product.getPrice()).thenReturn(new BigDecimal("10.0"));
                });
        productDao.getProducts().addAll(0, mockedProducts);
    }

    @Test
    public void givenProductWithDescription_whenFindProducts_thenGetProduct() {
        when(mockedProduct.getDescription()).thenReturn("testing");

        List<Product> products = productDao.sortProducts(SortField.DESCRIPTION, SortOrder.DESC, "testing");

        assertFalse(products.isEmpty());
        assertTrue(products.contains(mockedProduct));
        assertFalse(products.contains(anotherMockedProduct));
    }

    @Test
    public void givenProductWithZeroStock_whenFindProducts_thenGetProducts() {
        when(mockedProduct.getStock()).thenReturn(0);

        List<Product> products = productDao.sortProducts(SortField.DESCRIPTION, SortOrder.DESC, null);

        assertFalse(products.isEmpty());
        assertFalse(products.contains(mockedProduct));
    }

    @Test
    public void givenProductWithNullPrice_whenFindProducts_thenGetProducts() {
        when(mockedProduct.getPrice()).thenReturn(null);

        List<Product> products = productDao.sortProducts(SortField.PRICE, SortOrder.ASC, null);

        assertFalse(products.isEmpty());
        assertFalse(products.contains(mockedProduct));
    }

    @Test
    public void givenProduct_whenDeleteProduct_thenGetProduct() {
        productDao.delete(mockedProduct.getId());
        List<Product> products = productDao.sortProducts(SortField.DESCRIPTION, SortOrder.DESC, null);

        assertFalse(products.contains(mockedProduct));
    }

    @Test
    public void givenProduct_whenGetProductById_thenGetProduct() {
        Optional<Product> expectedProduct = productDao.getProductById(mockedProduct.getId());

        assertTrue(expectedProduct.isPresent());
        assertEquals(expectedProduct.get(), mockedProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullProduct_whenSaving_thenGetException() {
        productDao.save(null);
    }

    @Test
    public void givenProduct_whenSaving_thenGetProduct() {
        Product productToSave = Mockito.mock(Product.class);

        productDao.save(productToSave);
        List<Product> products = productDao.getProducts();

        assertNotNull(products);
        assertTrue(products.contains(productToSave));
    }

}
