package com.fatihsahin.order_tracking.dto.ProductDto;


import java.math.BigDecimal;
import java.util.List;


public record ProductResponseDto(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        List<OrdersDto> orders) {
}