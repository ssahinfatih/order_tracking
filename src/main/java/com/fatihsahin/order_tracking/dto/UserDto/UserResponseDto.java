package com.fatihsahin.order_tracking.dto.UserDto;

import com.fatihsahin.order_tracking.entities.Orders;

import java.util.List;

public record UserResponseDto(
        String name,
        String surname,
        String email,
        List<Orders> orders
) {
}
