package com.fatihsahin.order_tracking.mapper;

import com.fatihsahin.order_tracking.dto.ProductDto.ProductRequestDto;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductResponseDto;
import com.fatihsahin.order_tracking.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductRequestDto productRequestDto);

    ProductResponseDto toProductResponseDto(Product product);

    List<ProductResponseDto> toProductResponseDtoList(List<Product> products);

    void updateProductFromDto(
            ProductRequestDto productRequestDto,
            @MappingTarget Product product
    );
}