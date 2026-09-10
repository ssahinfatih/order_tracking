package com.fatihsahin.order_tracking.controller.impl;

import com.fatihsahin.order_tracking.dto.*;
import com.fatihsahin.order_tracking.entities.RefreshToken;
import com.fatihsahin.order_tracking.entities.UserLogin;
import com.fatihsahin.order_tracking.security.JwtService;
import com.fatihsahin.order_tracking.service.UserLoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserLoginController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserLoginService userLoginService;

    public UserLoginController(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserLoginService userLoginService) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userLoginService = userLoginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        userLoginRequestDto.getUsername()
                );

        if (!passwordEncoder.matches(
                userLoginRequestDto.getPassword(),
                userDetails.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Username veya password yanlış");
        }

        //String token = jwtService.generateToken(userDetails);
        TokenPair tokenPair= jwtService.generateTokenPair(userDetails);
        return ResponseEntity.ok(Map.of(
                "Sonuç","Login Başarılı",
                "accessToken", tokenPair.accessToken(),
                "refreshToken", tokenPair.refreshToken(),
                "User Name", userDetails.getUsername()
        ));
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        String username = jwtService.refreshAccessToken(request.refreshToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        TokenPair tokenPair= jwtService.generateTokenPair(userDetails);

        return ResponseEntity.ok(Map.of(
                "Sonuç","Token Yenilendi",
                "Access Token", tokenPair.accessToken(),
                "Refresh Token", tokenPair.refreshToken()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
        String username = jwtService.getUsernameFromToken(request.accessToken());
        jwtService.revokeAllRefreshTokens(username);
        return ResponseEntity.ok(Map.of("Sonuç","Logout Başarılı"));

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserLogin userLogin) {

        return ResponseEntity.ok(
                userLoginService.register(userLogin)
        );
    }
}