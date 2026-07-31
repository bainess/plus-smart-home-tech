package ru.yandex.practicum.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.entity.Product;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.mapper.ProductMapper;
import ru.yandex.practicum.product.repository.CategoryRepository;
import ru.yandex.practicum.product.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> getAllActiveProducts() {
        List<Product> products = productRepository.findAllByActiveTrue();
        return products.stream().map(ProductMapper::mapToProductDto).toList();
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + "not found"));

        return ProductMapper.mapToProductDto(product);
    }

    public List<ProductDto> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return products.stream().map(ProductMapper::mapToProductDto).toList();
    }

    public List<ProductDto> getProductsByName(String name) {
        List<Product> products = productRepository.findAllByName(name);
        return products.stream().map(ProductMapper::mapToProductDto).toList();
    }

    public ProductDto createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Product " + request.categoryId() + "not found"));
        Product product = ProductMapper.mapToProduct(request, category);

        product = productRepository.save(product);
        return ProductMapper.mapToProductDto(product);
    }


}
