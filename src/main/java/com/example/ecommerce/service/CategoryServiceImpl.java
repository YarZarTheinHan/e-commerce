package com.example.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService{

    private List<Category> categories = new ArrayList<>();
    private long nextId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public String createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
        return "Added category successfully!";
        
    }

    @Override
    public String deleteCategory(long categoryId){
        Category category = categories.stream()
        .filter(c -> Long.valueOf(c.getCategoryId()).equals(categoryId))
        .findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found!"));

        categories.remove(category);
        return "Category ID deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId){
        Optional<Category> optionalCategory = categories.stream().filter
        (c -> c.getCategoryId() == categoryId).findFirst();
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found To Update");
        }

    }
    

}
