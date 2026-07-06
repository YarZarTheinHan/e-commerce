package com.example.ecommerce.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
    
    private final CategoryRepository categoryRepository;

    CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category){
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category you delete was not found!"));
        categoryRepository.delete(category);
        return "Category ID deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId){
        category.setCategoryId(categoryId);
       return categoryRepository.save(category);
    }
    

}
