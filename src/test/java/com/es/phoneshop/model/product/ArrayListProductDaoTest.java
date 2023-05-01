package com.es.phoneshop.model.product;

import com.es.phoneshop.DAO.CustomProductDao;
import com.es.phoneshop.DAO.ProductDao;
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
public class ArrayListProductDaoTest {// i suppose service tests must be also written
    Currency usd = Currency.getInstance("USD");
    ProductDao productDao;
    @Mock
    ProductDao productDaoMock;
    Product product;
    List<Product> productList;

    @Before
    public void setup() {
        productDao = CustomProductDao.getCustomProductDao();

        product = new Product("test", "test", new BigDecimal(100), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productList = CustomProductDao.getCustomProductDao().getProducts();
        doAnswer(invocation -> {//mocking save
            Product productToSave = invocation.getArgument(0);
            productList.add(productToSave);
            return null;
        }).when(productDaoMock).save(any(Product.class));
        productDaoMock.save(product);
    }

    @After
    public void rearrange() {
        productList.remove(product);
    }

    @Test
    public void givenAProductWithZeroStock_whenFindProductsWithoutZeroStock_thenCheckExistenceOfGivenProduct() {
        product.setStock(0);

        List<Product> products = productDao.findProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.stream()
                .anyMatch(product_ -> product_.equals(product)));
    }

    @Test
    public void givenAProductWithNullPrice_whenFindProductsWithoutNullPrice_thenCheckExistenceOfGivenProduct() {
        product.setPrice(null);

        List<Product> products = productDao.findProducts();

        assertFalse(products.isEmpty());
        assertFalse(products.stream()
                .anyMatch(product_ -> product_.equals(product)));
    }

    @Test
    public void givenAProduct_whenDeleteGivenProduct_thenCheckExistenceOfGivenProduct() {
        List<Product> productsBeforeDel = CustomProductDao.getCustomProductDao().getProducts();
        productDao.delete(product.getId());
        List<Product> productsAfterDel = CustomProductDao.getCustomProductDao().getProducts();

        assertNotEquals(productsBeforeDel, productsAfterDel);
        assertTrue(productsBeforeDel.stream()
                .anyMatch(product_ -> product_.equals(product)));
        assertFalse(productsAfterDel.stream()
                .anyMatch(product_ -> product_.equals(product)));
    }

    @Test(expected = NoSuchElementException.class)
    public void givenAnId_whenGetProductByID_thenGetAnException() {
        productDao.getProductById(-1L).get();
    }

    @Test
    public void givenAProduct_whenGetProductById_thenGetThisProduct() {
        Product gottenProduct = productDao.getProductById(product.getId()).get();

        assertNotNull(gottenProduct);
        assertSame(product, gottenProduct);
    }

    @Test
    public void givenAProduct_whenGetProductByDescription_thenGetThisProduct() {
        Product gottenProduct = productDao.getProductByDescription(product.getDescription()).get(0);

        assertNotNull(gottenProduct);
        assertSame(product, gottenProduct);
    }

    @Test(expected = NullPointerException.class)
    public void givenANullProduct_whenSaving_thenGetAnException() {
        productDao.save(null);
    }

    @Test
    public void givenAProduct_whenChangingState_thenGetState() {
        CustomProductDao.getCustomProductDao().changeChosenState(product, true);
        assertNotNull(product.getIsChosen());
        assertTrue(product.getIsChosen());
    }
}
