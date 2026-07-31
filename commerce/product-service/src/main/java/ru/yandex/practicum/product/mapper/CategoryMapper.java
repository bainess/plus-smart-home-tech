package ru.yandex.practicum.product.mapper;

import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.entity.Category;

public class CategoryMapper {

    public static Category mapToCategory(CreateCategoryRequest dto) {
        Category category = Category.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
        return category;
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
        return dto;
    }
}
