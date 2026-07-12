package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponseDTO;

public interface CategoryService {
    CategoryResponseDTO getAllCategories();

    void createCategory(Category category);

    String deleteCategory(long categoryId);

    Category updateCategory(Category category, Long categoryId);
}
