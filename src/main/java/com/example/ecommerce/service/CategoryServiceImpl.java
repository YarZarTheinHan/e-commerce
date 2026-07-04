package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

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
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream()
        .filter(c -> Long.valueOf(c.getCategoryId()).equals(categoryId))
        .findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found!"));

        categoryRepository.delete(category);
        return "Category ID deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId){
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> optionalCategory = categories.stream().filter
        (c -> c.getCategoryId() == categoryId).findFirst();
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return categoryRepository.save(existingCategory);
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found To Update");
        }

    }
    

}
