package com.fatihsahin.order_tracking.controller.impl;

import com.fatihsahin.order_tracking.controller.IProductController;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductRequestDto;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductResponseDto;
import com.fatihsahin.order_tracking.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductControllerImpl implements IProductController {

    private final IProductService productService;

    public ProductControllerImpl(IProductService productService) {
        this.productService = productService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto productById = productService.getProductById(id);
        return ResponseEntity.ok().body(productById);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto) {

        ProductResponseDto createdProduct = productService.createProduct(productRequestDto);
        return ResponseEntity.ok().body(createdProduct);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto productRequestDto) {

        ProductResponseDto updatedProduct =
                productService.updateProduct(id, productRequestDto);

        return ResponseEntity.ok().body(updatedProduct);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductResponseDto> products =
                productService.getAllProducts(page, size);

        return ResponseEntity.ok().body(products);
    }
}