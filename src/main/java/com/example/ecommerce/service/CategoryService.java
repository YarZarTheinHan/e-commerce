package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.model.Category;

public interface CategoryService {
    List<Category> getAllCategories();

    String createCategory(Category category);

    String deleteCategory(long categoryId);
}
