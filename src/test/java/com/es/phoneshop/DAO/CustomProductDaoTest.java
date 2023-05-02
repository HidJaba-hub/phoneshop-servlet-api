package com.es.phoneshop.DAO;

import com.es.phoneshop.exception.ProductDefinitionException;
import com.es.phoneshop.model.entity.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CustomProductDaoTest {
    private final Currency usd = Currency.getInstance("USD");
    private ProductDao productDao;
    private Product product;
    private List<Product> productList;

    @Before
    public void setup() {
        productDao = CustomProductDao.getInstance();

        product = new Product("test", "test", new BigDecimal(100), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productList = CustomProductDao.getInstance().getProducts();
        productList.add(product);//as it's a reference we can add products this way to productDao, i suppose, without using save() and other method from test object
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
        productDao.delete(product.getId());
        List<Product> products = productDao.findProducts();

        assertFalse(products.contains(product));
    }

    @Test
    public void givenProduct_whenGetProductById_thenGetProduct() {
        Product expectedProduct = productDao.getProductById(product.getId()).get();

        assertNotNull(expectedProduct);
        assertEquals(expectedProduct, product);
    }

    @Test(expected = ProductDefinitionException.class)
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
        productDao.changeChosenState(product, true);
        assertNotNull(product.getIsChosen());
        assertTrue(product.getIsChosen());
    }

}
