package com.example.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

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
    

}
