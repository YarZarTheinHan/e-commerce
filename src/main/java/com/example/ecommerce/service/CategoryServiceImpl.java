package com.example.ecommerce.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.exceptions.APIException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
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
        List<Category> findAllCategory = categoryRepository.findAll();
        int sizeOfList = findAllCategory.size();
        if(sizeOfList > 0 ){
            return findAllCategory;
        }
        throw new APIException("Category does not exist");
    }

    @Override
    public void createCategory(Category category){
        Category categoryName = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryName != null){
            throw new APIException("Category Named "+category.getCategoryName()+" already exist!");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(
            "Category", "CategoryId",categoryId));
        categoryRepository.delete(category);
        return "Category ID deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId){
         Category categoryName = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryName != null){
            throw new APIException("Category Named "+category.getCategoryName()+" already exist!");
        }
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(
            "Category", "CategoryId", categoryId));
        category.setCategoryId(categoryId);
        return categoryRepository.save(category);
    }
    

}
