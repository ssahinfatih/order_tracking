package com.fatihsahin.order_tracking.dto.ProductDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


import java.math.BigDecimal;

public record ProductRequestDto(

        @NotBlank(message = "Name alanı boş bırakılamaz")
        String name,

        String description,

        @Min(message = "minimum 100 olamalıdır", value = 100)
        @Max(message = "maximum 500000 olmalıdır", value = 500000)
        BigDecimal price,

        @Min(message = "minimum 1 olmalıdır", value = 1)
        @Max(message = "maximum 1000 olmalıdır", value = 1000)
        Integer stock

) {
}