package com.example.ecommerce.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CategoryResponseDTO getAllCategories(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        Sort sortByOrder = sortOrder.equalsIgnoreCase("acs") ? 
        Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByOrder);
        Page<Category> pageCategory = categoryRepository.findAll(pageDetails);
        List<Category> findAllCategory = pageCategory.getContent();
       // List<Category> findAllCategory = categoryRepository.findAll();
        int sizeOfList = findAllCategory.size();
        if(sizeOfList > 0 ){
           List<CategoryDTO> categoryDTOs = findAllCategory.stream().map(category -> modelMapper
            .map(category, CategoryDTO.class)).toList();

            CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
            categoryResponseDTO.setCategories(categoryDTOs);
            categoryResponseDTO.setPageNumber(pageNumber);
            categoryResponseDTO.setPageSize(pageSize);
            categoryResponseDTO.setTotalElement(pageCategory.getTotalElements());
            categoryResponseDTO.setTotalPages(pageCategory.getTotalPages());
            categoryResponseDTO.setLastPage(pageCategory.isLast());
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
    public CategoryDTO deleteCategory(long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(
            "Category", "CategoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        Category checkDuplicateName = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(checkDuplicateName != null) {
             throw new APIException("Category Named "+category.getCategoryName()+" already exist!");
        }
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
    

}
