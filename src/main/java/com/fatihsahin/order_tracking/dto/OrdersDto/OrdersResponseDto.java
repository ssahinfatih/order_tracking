package com.fatihsahin.order_tracking.dto.OrdersDto;

import com.fatihsahin.order_tracking.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdersResponseDto(
        String orderNumber,
        BigDecimal totalAmount,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserDto user,
        List<ProductDto> products){
}