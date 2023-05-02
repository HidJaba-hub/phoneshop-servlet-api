package com.es.phoneshop.service;

import com.es.phoneshop.DAO.CustomProductDao;
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
public class CustomProductServiceTest {
    private final Currency usd = Currency.getInstance("USD");
    private ProductService productService;
    private Product product;
    private List<Product> productList;

    @Before
    public void setup() {
        productService = CustomProductService.getInstance();

        product = new Product("test", "test", new BigDecimal(100), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productList = CustomProductDao.getInstance().getProducts();
        productList.add(product);
    }

    @After
    public void release() {
        productList.remove(product);
        CustomProductDao.getInstance().delete(product.getId());
    }

    @Test
    public void givenProductWithZeroStock_whenFindProducts_thenGetProducts() {
        product.setStock(0);

        List<Product> products = productService.getProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.contains(product));
    }

    @Test
    public void givenProductWithNullPrice_whenFindProducts_thenGetProducts() {
        product.setPrice(null);

        List<Product> products = productService.getProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.contains(product));
    }

    @Test
    public void givenProduct_whenDeleteProduct_thenGetProduct() {
        productService.deleteProduct(product.getId());
        List<Product> products = productService.getProducts();

        assertFalse(products.contains(product));
    }

    @Test(expected = ProductDefinitionException.class)
    public void givenId_whenGetProductByID_thenGetException() {
        productService.getProductById(-1L);
    }

    @Test
    public void givenProduct_whenGetProductById_thenGetProduct() {
        Product expectedProduct = productService.getProductById(product.getId());

        assertNotNull(expectedProduct);
        assertEquals(expectedProduct, product);
    }

    @Test
    public void givenProduct_whenSaving_thenGetProduct() {
        Product productToSave = new Product("savetest", "savetest", new BigDecimal(100), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productService.saveProduct(productToSave);

        assertNotNull(productToSave.getId());
        assertTrue(productService.getProducts().contains(productToSave));
    }

    @Test
    public void givenProduct_whenChangingState_thenGetState() {
        productService.changeState(product, true);
        assertNotNull(product.getIsChosen());
        assertTrue(product.getIsChosen());
    }
}
