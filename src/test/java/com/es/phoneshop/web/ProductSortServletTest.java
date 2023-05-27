package com.es.phoneshop.web;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.service.product.ProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductSortServletTest {

    @InjectMocks
    private final ProductSortServlet servlet = new ProductSortServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig config;
    @Mock
    private ProductService productService;
    @Mock
    private PrintWriter printWriter;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setProductService(productService);
    }

    @Test
    public void givenSortAndQuery_whenDoGet_thenVerifyGetProductsWithSorting() throws IOException {
        when(request.getParameter("order")).thenReturn(SortOrder.DESC.toString());
        when(request.getParameter("sort")).thenReturn(SortField.DESCRIPTION.toString());
        when(request.getParameter("query")).thenReturn("query");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(productService).getProductsWithSortingAndQuery(SortField.DESCRIPTION, SortOrder.DESC, "query");
        verify(response).getWriter();
    }
}
