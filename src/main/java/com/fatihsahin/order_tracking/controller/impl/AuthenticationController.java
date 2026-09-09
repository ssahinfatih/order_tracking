package com.fatihsahin.order_tracking.controller.impl;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthenticationController {

    // Kullanıcı bilgilerini almak için bir endpoint oluşturuyoruz
    @GetMapping("/api/v1/profile")
    public ResponseEntity<?> getProfile(@CurrentSecurityContext SecurityContext securityContext) {// @CurrentSecurityContext ile güvenlik bağlamını alıyoruz
        Authentication authentication =  securityContext.getAuthentication();// Authentication nesnesi ile kullanıcı bilgilerini alıyoruz
        return ResponseEntity.ok(Map.of(// Map.of ile kullanıcı bilgilerini döndürüyoruz
                "username", authentication.getName(),// getName() ile kullanıcı adını alıyoruz
                "authorities", authentication.getAuthorities()//    getAuthorities() ile kullanıcı yetkilerini alıyoruz
        ));
    }

    @GetMapping("/api/v1/profile/name")// Kullanıcı adını almak için bir endpoint oluşturuyoruz
    public ResponseEntity<?> getProfileName(@CurrentSecurityContext(expression = "authentication.name") String name) {// @CurrentSecurityContext ile güvenlik bağlamını alıyoruz ve expression ile kullanıcı adını alıyoruz
        return ResponseEntity.ok(Map.of("username", name));// Map.of ile kullanıcı adını döndürüyoruz
    }

}
