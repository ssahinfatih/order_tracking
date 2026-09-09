package com.fatihsahin.order_tracking.controller.impl;

import com.fatihsahin.order_tracking.dto.UserLoginRequestDto;
import com.fatihsahin.order_tracking.entities.UserLogin;
import com.fatihsahin.order_tracking.security.JwtService;
import com.fatihsahin.order_tracking.service.UserLoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
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

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserLogin userLogin) {

        return ResponseEntity.ok(
                userLoginService.register(userLogin)
        );
    }
}