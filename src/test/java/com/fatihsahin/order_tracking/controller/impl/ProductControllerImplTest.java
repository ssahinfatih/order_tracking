package com.fatihsahin.order_tracking.controller.impl;

import com.fatihsahin.order_tracking.dto.ProductDto.OrdersDto;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductResponseDto;
import com.fatihsahin.order_tracking.enums.PaymentStatus;
import com.fatihsahin.order_tracking.security.JwtService;
import com.fatihsahin.order_tracking.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value =  ProductControllerImpl.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)//security config devre dışı bırakıldı
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerImplTest {

    ProductResponseDto productResponseDto;
    OrdersDto ordersDto;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IProductService productService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @MockitoBean
    CacheManager cacheManager;

    @MockitoBean
    JwtService jwtService;

    @BeforeEach
    void setUp() {

        ordersDto = new OrdersDto(
                "123456",
                new BigDecimal("1250.5"),
                PaymentStatus.PAID,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productResponseDto = new ProductResponseDto(
                "Kalem",
                "Kurşun Kalem",
                new BigDecimal("444.5"),
                30,
                List.of(ordersDto)
        );
    }

    @Test
    void getProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productResponseDto);
        mockMvc.perform(get("/api/v1/products/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kalem"))
                .andExpect(jsonPath("$.description").value("Kurşun Kalem"))
                .andExpect(jsonPath("$.price").value(new BigDecimal("444.5")))
                .andExpect(jsonPath("$.stock").value(30))
                .andExpect(jsonPath("$.orders[0].orderNumber").value("123456"))
                .andExpect(jsonPath("$.orders[0].totalAmount").value(1250.5))
                .andExpect(jsonPath("$.orders[0].paymentStatus").value("PAID"));

    }
}