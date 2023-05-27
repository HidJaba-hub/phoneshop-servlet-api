package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.CustomDosProtectionService;
import com.es.phoneshop.security.DosProtectionService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DosFilter implements Filter {

    private final int TOO_MANY_REQUEST_STATUS = 429;
    private DosProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosProtectionService = CustomDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (dosProtectionService.isAllowed(servletRequest.getRemoteAddr())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendError(TOO_MANY_REQUEST_STATUS);
        }
    }

    @Override
    public void destroy() {

    }
}
