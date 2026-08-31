package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.config.AppConstant;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponseDTO;
import com.example.ecommerce.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;




@RequestMapping("api/")
@RestController
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public String requestMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/echo")
    public ResponseEntity<String> testParameter(@RequestParam(name="name", defaultValue = "Hi Default") String name){
        return ResponseEntity.ok("Parameter Value: "+name);
    }

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
        @RequestParam(name = "pageSize", defaultValue=AppConstant.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "pageNumber", defaultValue=AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_ORDER) String sortOrder
    ) {
        CategoryResponseDTO categoryResponse = categoryService.getAllCategories(pageSize, pageNumber, sortBy, sortOrder);
        return ResponseEntity.ok(categoryResponse);
    }

    @PostMapping("public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public  ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(categoryDTO);
    }
    
    @PutMapping("public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
        CategoryDTO updateCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updateCategoryDTO, HttpStatus.OK);
  }
}
