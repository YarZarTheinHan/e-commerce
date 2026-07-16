package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponseDTO;

public interface CategoryService {
    CategoryResponseDTO getAllCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    String deleteCategory(long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
