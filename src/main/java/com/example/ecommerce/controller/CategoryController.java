package com.example.ecommerce.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("api/")
@RestController
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Added category successfully!", HttpStatus.CREATED);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public  ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try{
        String status = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(status);
        } catch(ResponseStatusException e){
           return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
    
    @PutMapping("public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
    try{
        Category updateCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>("Updated Category Id:" + categoryId, HttpStatus.ACCEPTED);
    } catch(ResponseStatusException e){
       return  new ResponseEntity<>(e.getReason(), e.getStatusCode());
    }
  }
    

}
