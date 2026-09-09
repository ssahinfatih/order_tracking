package com.fatihsahin.order_tracking.service.impl;

import com.fatihsahin.order_tracking.entities.UserLogin;
import com.fatihsahin.order_tracking.repository.UserLoginRepository;
import com.fatihsahin.order_tracking.service.UserLoginService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;

    public UserLoginServiceImpl(
            UserLoginRepository userLoginRepository,
            PasswordEncoder passwordEncoder) {

        this.userLoginRepository = userLoginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserLogin register(UserLogin userLogin) {

        userLogin.setPassword(
                passwordEncoder.encode(userLogin.getPassword())
        );

        return userLoginRepository.save(userLogin);
    }
}