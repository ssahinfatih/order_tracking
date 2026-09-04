package com.fatihsahin.order_tracking.service;


import com.fatihsahin.order_tracking.dto.ProductDto.ProductRequestDto;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductResponseDto;
import org.springframework.data.domain.Page;

public interface IProductService {

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);

    Page<ProductResponseDto> getAllProducts(int page, int size);
}