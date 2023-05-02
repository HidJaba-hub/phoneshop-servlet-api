package com.es.phoneshop.service;

import com.es.phoneshop.DAO.CustomProductDao;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class CustomProductServiceTest {
    private final Currency usd = Currency.getInstance("USD");
    private ProductService productService;
    @Mock
    private ProductService productServiceMock;
    private Product product;
    private List<Product> productList;

    @Before
    public void setup() {
        productService = CustomProductService.getInstance();

        product = new Product("test", "test", new BigDecimal(100), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productList = CustomProductService.getInstance().getProducts();
        doAnswer(invocation -> {//mocking save
            Product productToSave = invocation.getArgument(0);
            CustomProductDao.getInstance().save(product);
            productList.add(productToSave);
            return null;
        }).when(productServiceMock).saveProduct(any(Product.class));
        productServiceMock.saveProduct(product);
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
        List<Product> productsBeforeDel = CustomProductService.getInstance().getProducts();
        productService.deleteProduct(product.getId());
        List<Product> productsAfterDel = CustomProductService.getInstance().getProducts();

        assertNotEquals(productsBeforeDel, productsAfterDel);
        assertTrue(productsBeforeDel.contains(product));
        assertFalse(productsAfterDel.contains(product));
    }

    @Test(expected = NoSuchElementException.class)
    public void givenId_whenGetProductByID_thenGetException() {
        productService.getProductById(-1L);
    }

    @Test
    public void givenProduct_whenGetProductById_thenGetProduct() {
        Product expectedProduct = productService.getProductById(product.getId());

        assertNotNull(expectedProduct);
        assertEquals(product, expectedProduct);
    }

    @Test
    public void givenProduct_whenGetProductByDescription_thenGetProduct() {
        Product expectedProduct = productService.getProductByDescription(product.getDescription()).get(0);

        assertNotNull(expectedProduct);
        assertEquals(product, expectedProduct);
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
        CustomProductService.getInstance().changeState(product, true);
        assertNotNull(product.getIsChosen());
        assertTrue(product.getIsChosen());
    }
}
