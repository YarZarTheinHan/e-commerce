package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponseDTO;

public interface ProductService {
     ProductDTO addProduct(Long categoryId,Product product);

     ProductResponseDTO getAllProducts();

     ProductResponseDTO getProductsByCategory(Long categoryId);
}
