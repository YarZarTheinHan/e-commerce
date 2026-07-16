package com.example.ecommerce.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exceptions.APIException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponseDTO;
import com.example.ecommerce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
    
    private final CategoryRepository categoryRepository;

    CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    private final ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getAllCategories() {
        List<Category> findAllCategory = categoryRepository.findAll();
        int sizeOfList = findAllCategory.size();
        if(sizeOfList > 0 ){
           List<CategoryDTO> categoryDTOs = findAllCategory.stream().map(category -> modelMapper
            .map(category, CategoryDTO.class)).toList();

            CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
            categoryResponseDTO.setCategories(categoryDTOs);
            return categoryResponseDTO;
        }
        throw new APIException("Category does not exist");
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO){
       Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryfromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryfromDB != null){
            throw new APIException("Category Named "+category.getCategoryName()+" already exist!");
        }
        Category saveCategory = categoryRepository.save(category);
        return modelMapper.map(saveCategory, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(
            "Category", "CategoryId",categoryId));
        categoryRepository.delete(category);
        return "Category ID deleted successfully";
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId){
        modelMapper.map(categoryDTO, Category.class);
         Category categoryName = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (categoryName != null){
            throw new APIException("Category Named "+categoryDTO.getCategoryName()+" already exist!");
        }
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(
            "Category", "CategoryId", categoryId));

            modelMapper.map(savedCategory, CategoryDTO.class);
            categoryDTO.setCategoryId(categoryId);
            Category category = categoryRepository.save(categoryDTO);
    }
    

}
