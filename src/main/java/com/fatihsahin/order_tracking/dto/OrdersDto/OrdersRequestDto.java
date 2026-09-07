package com.fatihsahin.order_tracking.dto.OrdersDto;

import com.fatihsahin.order_tracking.enums.PaymentStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;



public record OrdersRequestDto(
        @NotBlank
        @Min(message = "minimum 1", value = 1)
        String orderNumber,
        @NotNull
        @Min(message = "minimum 1", value = 1)
        @Max(message = "maximum 1000000", value = 1000000)
        BigDecimal totalAmount,
        PaymentStatus paymentStatus,
        UserDto user,
        ProductDto product
       )  {
}