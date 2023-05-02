package com.es.phoneshop.DAO;

import com.es.phoneshop.exception.ProductException;
import com.es.phoneshop.model.entity.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private final Currency usd = Currency.getInstance("USD");
    private ProductDao productDao;
    @Mock
    private ProductDao productDaoMock;
    private Product product;
    private List<Product> productList;

    @Before
    public void setup() {
        productDao = CustomProductDao.getInstance();

        product = new Product("test", "test", new BigDecimal(100), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productList = CustomProductDao.getInstance().getProducts();
        doAnswer(invocation -> {//mocking save
            Product productToSave = invocation.getArgument(0);
            productList.add(productToSave);
            return null;
        }).when(productDaoMock).save(any(Product.class));
        productDaoMock.save(product);
    }

    @After
    public void release() {
        productList.remove(product);
    }

    @Test
    public void givenProductWithZeroStock_whenFindProducts_thenGetProducts() {
        product.setStock(0);

        List<Product> products = productDao.findProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.contains(product));
    }

    @Test
    public void givenProductWithNullPrice_whenFindProducts_thenGetProducts() {
        product.setPrice(null);

        List<Product> products = productDao.findProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.contains(product));
    }

    @Test
    public void givenProduct_whenDeleteProduct_thenGetProduct() {
        List<Product> productsBeforeDel = CustomProductDao.getInstance().getProducts();
        productDao.delete(product.getId());
        List<Product> productsAfterDel = CustomProductDao.getInstance().getProducts();

        assertNotEquals(productsBeforeDel, productsAfterDel);
        assertTrue(productsBeforeDel.contains(product));
        assertFalse(productsAfterDel.contains(product));
    }

    @Test(expected = NoSuchElementException.class)
    public void givenId_whenGetProductByID_thenGetException() {
        productDao.getProductById(-1L).get();
    }

    @Test
    public void givenProduct_whenGetProductById_thenGetProduct() {
        Product expectedProduct = productDao.getProductById(product.getId()).get();

        assertNotNull(expectedProduct);
        assertEquals(product, expectedProduct);
    }

    @Test
    public void givenProduct_whenGetProductByDescription_thenGetProduct() {
        Product expectedProduct = productDao.getProductByDescription(product.getDescription()).get(0);

        assertNotNull(expectedProduct);
        assertEquals(product, expectedProduct);
    }

    @Test(expected = ProductException.class)
    public void givenNullProduct_whenSaving_thenGetException() {
        productDao.save(null);
    }

    @Test
    public void givenProduct_whenSaving_thenGetProduct() {
        Product productToSave = new Product("savetest", "savetest", new BigDecimal(100), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productToSave);

        assertNotNull(productToSave.getId());
        assertTrue(productList.contains(productToSave));
        productList.remove(productToSave);
    }

    @Test
    public void givenProduct_whenChangingState_thenGetState() {
        CustomProductDao.getInstance().changeChosenState(product, true);
        assertNotNull(product.getIsChosen());
        assertTrue(product.getIsChosen());
    }

}
