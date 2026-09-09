package com.fatihsahin.order_tracking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserLoginRequestDto{
    @NotNull
    @Size(min = 3, max = 50)
    String username;
    @NotNull
    @Size(min = 3, max = 50)
    String password;
}
