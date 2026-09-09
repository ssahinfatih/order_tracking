package com.fatihsahin.order_tracking.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,// HttpServletRequest sınıfını kullanarak request nesnesini alıyoruz
            HttpServletResponse response,// HttpServletResponse sınıfını kullanarak response nesnesini alıyoruz
            FilterChain filterChain) throws IOException, ServletException {// FilterChain sınıfını kullanarak filterChain nesnesini alıyoruz

        String header = request.getHeader("Authorization");// Authorization header'ını alıyoruz
        if (header == null || !header.startsWith("Bearer ")) {//    Authorization header'ı null veya Bearer ile başlamıyorsa filtre
            filterChain.doFilter(request, response);// filterChain ile request ve response nesnelerini filtreliyoruz
            return;
        }
        String token = header.substring(7);// Authorization header'ından tokeni alıyoruz
        String username = jwtService.getUsernameFromToken(token);// token'dan kullanıcı adını alıyoruz
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);//
            if (jwtService.validateToken(token, userDetails)) {// token geçerliyse kullanıcıyı authenticate ediyoruz
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());// UsernamePasswordAuthenticationToken ile kullanıcıyı authenticate ediyoruz
                SecurityContextHolder.getContext().setAuthentication(authentication);// SecurityContextHolder ile kullanıcıyı authenticate ediyoruz
            }
        }
        filterChain.doFilter(request, response);
    }
}
