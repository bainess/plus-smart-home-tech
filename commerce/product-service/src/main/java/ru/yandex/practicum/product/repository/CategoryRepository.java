package ru.yandex.practicum.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.entity.Product;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
