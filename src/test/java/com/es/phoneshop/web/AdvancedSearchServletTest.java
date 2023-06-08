package com.es.phoneshop.web;

import com.es.phoneshop.validators.ParseValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdvancedSearchServletTest {

    @InjectMocks
    private AdvancedSearchServlet servlet = new AdvancedSearchServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ParseValidator parseValidator;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        MockitoAnnotations.initMocks(this);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenEmptyData_whenDoGet_thenVerifyForward() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("products"), any());
        verify(request).setAttribute(eq("searchCriteria"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenValidPrice_whenDoGet_thenVerifyForward() throws ServletException, IOException {
        when(request.getParameter("minPrice")).thenReturn("100");
        when(request.getParameter("searchCriteria")).thenReturn("ANY_WORDS");

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("products"), any());
        verify(request).setAttribute(eq("searchCriteria"), any());
        verify(request).setAttribute(eq("message"), anyString());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenInvalidPrice_whenDoGet_thenVerifyForward() throws ServletException, IOException {
        when(request.getParameter("minPrice")).thenReturn("a");
        when(request.getParameter("searchCriteria")).thenReturn("ANY_WORDS");

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("products"), any());
        verify(request).setAttribute(eq("searchCriteria"), any());
        verify(request).setAttribute(eq("errors"), any());
        verify(requestDispatcher).forward(request, response);
    }
}
