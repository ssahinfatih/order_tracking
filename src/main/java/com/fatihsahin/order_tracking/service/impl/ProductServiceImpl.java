package com.fatihsahin.order_tracking.service.impl;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductRequestDto;
import com.fatihsahin.order_tracking.dto.ProductDto.ProductResponseDto;
import com.fatihsahin.order_tracking.entities.Product;
import com.fatihsahin.order_tracking.exception.NotFoundException;
import com.fatihsahin.order_tracking.mapper.ProductMapper;
import com.fatihsahin.order_tracking.repository.ProductRepository;
import com.fatihsahin.order_tracking.service.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Bulunamadı: " + id));

        return productMapper.toProductResponseDto(product);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toProduct(productRequestDto);

        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Bulunamadı: " + id));

        productMapper.updateProductFromDto(productRequestDto, product);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toProductResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<Product> productsPage = productRepository.findAll(pageable);

        return productsPage.map(productMapper::toProductResponseDto);
    }
}
