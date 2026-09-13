package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponseDTO;

public interface ProductService {
     ProductDTO addProduct(Long categoryId,ProductDTO productDTO);

     ProductResponseDTO getAllProducts();

     ProductResponseDTO getProductsByCategory(Long categoryId);

     ProductResponseDTO searchProductByKeywords(String keyword);

     ProductDTO updateProducts(ProductDTO productDTO, Long productId);

     ProductDTO deleteProduct(Long productId);
}
