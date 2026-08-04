package ru.yandex.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllActiveProducts() {
        return productService.getAllActiveProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProductsByCategory(
            @PathVariable Long categoryId) {

        return productService.getProductsByCategoryId(categoryId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProductsByName(
            @RequestParam("query") String query) {

        return productService.getProductsByName(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(
            @RequestBody @Valid CreateProductRequest request) {

        return productService.createProduct(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateProductRequest request) {

        return productService.updateProduct(id, request);
    }
}
