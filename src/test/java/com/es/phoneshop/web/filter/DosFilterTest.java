package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DosProtectionService;
import jakarta.servlet.FilterChain;
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

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {

    private final int TOO_MANY_REQUEST_STATUS = 429;
    private final String ip = "111";
    @Mock
    private DosProtectionService dosProtectionService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @InjectMocks
    private DosFilter filter = new DosFilter();

    @Before
    public void setup() throws ServletException {
        when(request.getRemoteAddr()).thenReturn(ip);
    }

    @Test
    public void givenAddress_whenDoFilter_thenVerifyDoFilter() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(ip)).thenReturn(true);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void givenTooManyRequests_whenDoFilter_thenVerifySendError() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(ip)).thenReturn(false);

        filter.doFilter(request, response, chain);

        verify(response, atLeastOnce()).sendError(TOO_MANY_REQUEST_STATUS);
    }
}