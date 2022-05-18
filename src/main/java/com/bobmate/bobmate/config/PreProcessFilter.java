package com.bobmate.bobmate.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * GET POST 이외의 불필요한 요청을 받지않기 위핸 필터
 */
@Component
public class PreProcessFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!(request.getMethod().equalsIgnoreCase("GET") ||
                request.getMethod().equalsIgnoreCase("POST"))) {

            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

            return;
        }
        filterChain.doFilter(request, response);
    }
}
