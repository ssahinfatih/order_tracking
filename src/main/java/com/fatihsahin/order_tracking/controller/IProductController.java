package com.fatihsahin.order_tracking.controller;

import com.fatihsahin.order_tracking.dto.ProductDto.ProductRequestDto;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface IProductController {

    ResponseEntity<ProductResponseDto> getProductById(Long id);

    ResponseEntity<ProductResponseDto> createProduct(ProductRequestDto productRequestDto);

    ResponseEntity<ProductResponseDto> updateProduct(Long id, ProductRequestDto productRequestDto);

    ResponseEntity<Void> deleteProduct(Long id);

    ResponseEntity<Page<ProductResponseDto>> getAllProducts(int page, int size);
}