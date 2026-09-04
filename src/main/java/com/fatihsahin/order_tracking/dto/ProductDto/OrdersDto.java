package com.fatihsahin.order_tracking.dto.ProductDto;

import com.fatihsahin.order_tracking.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public record OrdersDto(
        String orderNumber,
        BigDecimal totalAmount,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt){
}