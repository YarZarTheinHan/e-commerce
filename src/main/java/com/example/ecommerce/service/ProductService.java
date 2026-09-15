package com.example.ecommerce.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponseDTO;

public interface ProductService {
     ProductDTO addProduct(Long categoryId,ProductDTO productDTO);

     ProductResponseDTO getAllProducts(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

     ProductResponseDTO getProductsByCategory(Long categoryId, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

     ProductResponseDTO searchProductByKeywords(String keyword,Integer pageSize, Integer pageNumber, String sortBy, String sortOrder);

     ProductDTO updateProducts(ProductDTO productDTO, Long productId);

     ProductDTO deleteProduct(Long productId);

     ProductDTO updateProductImage(Long productId, MultipartFile file) throws IOException;
}
