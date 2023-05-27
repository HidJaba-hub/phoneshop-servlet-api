package com.es.phoneshop.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterConfig filterConfig;
    @Mock
    private FilterChain chain;

    @InjectMocks
    private DosFilter filter = new DosFilter();

    @Before
    public void setup() throws ServletException {
        filter.init(filterConfig);
        when(request.getRemoteAddr()).thenReturn("111");
    }

    @Test
    public void givenAddress_whenDoFilter_thenVerifyDoFilter() throws ServletException, IOException {
        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

}