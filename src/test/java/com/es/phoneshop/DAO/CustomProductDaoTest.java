package com.es.phoneshop.DAO;

import com.es.phoneshop.exception.ProductDefinitionException;
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
        mockedProducts//set mocked values for both mockedProduct and anotherMockedProduct
                .forEach(product -> {
                    when(product.getStock()).thenReturn(10);
                    when(product.getPrice()).thenReturn(new BigDecimal("10.0"));
                });//i set only values that i need for checking. am i need to set all the values?
        productDao.setProducts(mockedProducts);//set new list with mocked products
    }

    @Test
    public void givenProductWithZeroStock_whenFindProducts_thenGetProducts() {
        when(mockedProduct.getStock()).thenReturn(0);

        List<Product> products = productDao.findProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.contains(mockedProduct));
    }

    @Test
    public void givenProductWithNullPrice_whenFindProducts_thenGetProducts() {
        when(mockedProduct.getPrice()).thenReturn(null);

        List<Product> products = productDao.findProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.contains(mockedProduct));
    }

    @Test
    public void givenProduct_whenDeleteProduct_thenGetProduct() {
        productDao.delete(mockedProduct.getId());
        List<Product> products = productDao.findProducts();

        assertFalse(products.contains(mockedProduct));
    }

    @Test
    public void givenProduct_whenGetProductById_thenGetProduct() {
        Optional<Product> expectedProduct = productDao.getProductById(mockedProduct.getId());

        assertTrue(expectedProduct.isPresent());
        assertEquals(expectedProduct.get(), mockedProduct);
    }

    @Test(expected = ProductDefinitionException.class)
    public void givenNullProduct_whenSaving_thenGetException() {
        productDao.save(null);
    }

    @Test
    public void givenProduct_whenSaving_thenGetProduct() {
        Product productToSave = Mockito.mock(Product.class);

        productDao.save(productToSave);

        assertNotNull(productDao.getProducts());
        assertTrue(productDao.getProducts().contains(productToSave));
    }

    @Test
    public void givenProduct_whenChangingState_thenGetState() {
        when(mockedProduct.getIsChosen()).thenReturn(true);

        productDao.changeChosenState(mockedProduct, true);

        assertNotNull(mockedProduct.getIsChosen());
        assertTrue(mockedProduct.getIsChosen());
    }
}
