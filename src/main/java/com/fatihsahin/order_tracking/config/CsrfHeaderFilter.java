package com.fatihsahin.order_tracking.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(// OncePerRequestFilter sınıfının doFilterInternal metodunu override ediyoruz
            HttpServletRequest request,// HttpServletRequest sınıfını kullanarak request nesnesini alıyoruz
            HttpServletResponse response,// HttpServletResponse sınıfını kullanarak response nesnesini alıyoruz
            FilterChain filterChain// FilterChain sınıfını kullanarak filterChain nesnesini alıyoruz
    ) throws ServletException, IOException {

        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());// CsrfToken sınıfını kullanarak CSRF tokenini alıyoruz
        if (csrf != null) {// CSRF tokeni null değilse response headerına ekliyoruz
            response.setHeader("X-CSRF-TOKEN", csrf.getToken());// response headerına CSRF tokenini ekliyoruz
        }
        filterChain.doFilter(request, response);// filterChain ile request ve response nesnelerini filtreliyoruz

    }
}
