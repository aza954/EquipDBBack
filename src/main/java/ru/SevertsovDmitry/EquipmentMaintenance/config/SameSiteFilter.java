package ru.SevertsovDmitry.EquipmentMaintenance.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

//@Component
public class SameSiteFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        chain.doFilter(request, response);

        String setCookieHeader = response.getHeader("Set-Cookie");
        if (setCookieHeader != null) {
            response.setHeader("Set-Cookie", setCookieHeader + "; SameSite=None; Secure");
        }
    }
}