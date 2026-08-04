package ru.yandex.practicum.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.mapper.CategoryMapper;
import ru.yandex.practicum.product.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto createCategory(CreateCategoryRequest request) {
        Category category = CategoryMapper.mapToCategory(request);
        category =  categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(category);
    }

    public CategoryDto getCategoryById(Long id) {
        Category category  = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category id " + id + " not found"));
        return CategoryMapper.mapToCategoryDto(category);
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(CategoryMapper::mapToCategoryDto).toList();
    }
}
