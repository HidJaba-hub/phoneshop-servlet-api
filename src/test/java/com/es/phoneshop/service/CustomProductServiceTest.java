package com.es.phoneshop.service;

import com.es.phoneshop.DAO.ProductDao;
import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.exception.ProductDefinitionException;
import com.es.phoneshop.model.entity.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private CustomProductService productService;

    @Before
    public void setup() {
        productService = CustomProductService.getInstance();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenListWithProduct_whenFindProducts_thenGetProducts() {
        List<Product> expectedList = new ArrayList<>();
        expectedList.add(new Product());
        when(productDao.findProducts(SortField.DESCRIPTION, SortOrder.DESC, "")).thenReturn(expectedList);

        List<Product> products = productService.getProducts(SortField.DESCRIPTION, SortOrder.DESC, "");

        assertEquals(expectedList, products);
    }

    @Test
    public void givenId_whenDeleteProduct_thenVerify() {
        productService.deleteProduct(1L);

        verify(productDao).delete(1L);
    }

    @Test(expected = ProductDefinitionException.class)
    public void givenId_whenGetProductByID_thenGetException() {
        productService.getProductById(-1L);
    }

    @Test
    public void givenProduct_whenGetProductById_thenGetProduct() {
        Product product = new Product();
        when(productDao.getProductById(product.getId())).thenReturn(Optional.of(product));

        Product expectedProduct = productService.getProductById(product.getId());

        assertEquals(expectedProduct, product);
    }

    @Test
    public void givenProduct_whenSaving_thenGetProduct() {
        Product productToSave = new Product();

        productService.saveProduct(productToSave);

        verify(productDao).save(productToSave);
    }

    @Test
    public void givenProduct_whenChangingState_thenGetState() {
        Product product = new Product();

        productService.changeState(product, true);

        assertNotNull(product.getIsChosen());
        assertTrue(product.getIsChosen());
    }
}
