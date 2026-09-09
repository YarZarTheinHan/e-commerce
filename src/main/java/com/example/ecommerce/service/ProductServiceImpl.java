package com.example.ecommerce.service;


import java.util.List;

import org.hibernate.internal.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponseDTO;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponseDTO;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;

@Service 
public class ProductServiceImpl implements ProductService{
    final ProductRepository productRepository;

    final CategoryRepository categoryRepository;

    final ModelMapper mapper;

    ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public ProductDTO addProduct(Long categoryId, Product product){
       Category  categories = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category Not Found","categoryId",categoryId));
        product.setImage("default.png");
        product.setCategory(categories);
        double specialPrice = product.getPrice()-((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDTO.class);
    }

    public ProductResponseDTO getAllProducts(){
        List<Product> product = productRepository.findAll();
        List<ProductDTO> productDTOs = product.stream().map(products -> mapper.map(products, ProductDTO.class)).toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOs);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductsByCategory(Long categoryId) {
       Category category = categoryRepository.findById(categoryId).
       orElseThrow( ()-> new ResourceNotFoundException("Category", "CategoryID",categoryId));

       List<Product> productByCategory = productRepository.findByCategoryOrderByPriceAsc(category);
       List<ProductDTO> productDTOs = productByCategory.stream()
                        .map(products->mapper.map(products, ProductDTO.class)).toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOs);
       return productResponseDTO;
    }
}
