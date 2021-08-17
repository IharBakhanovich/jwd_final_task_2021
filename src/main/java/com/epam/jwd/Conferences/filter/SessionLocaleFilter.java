package com.epam.jwd.Conferences.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters the Locale and fetches 'sessionLocale' parameter from the request.
 */
@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = "/*")
public class SessionLocaleFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Filter.super.init(filterConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (request.getParameter("sessionLocale") != null) {
            request.getSession(false).setAttribute("language", request.getParameter("sessionLocale"));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        //Filter.super.destroy();
    }
}

