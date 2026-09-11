package com.example.ecommerce.controller;


import com.example.ecommerce.repository.ProductRepository;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponseDTO;
import com.example.ecommerce.service.ProductService;

@RestController 
@RequestMapping("/api")
public class ProductController {

    private final ProductRepository productRepository;
    final ProductService productService;

    ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                @RequestBody  Product product){
        ProductDTO productDTO = productService.addProduct(categoryId, product);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProducts(){
       ProductResponseDTO product = productService.getAllProducts();
       return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductsByCategoryId(@PathVariable Long categoryId){
        ProductResponseDTO productResponseDTO = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ProductResponseDTO searchProductByKeywords(@PathVariable String keyword){
        ProductResponseDTO searchedProducts = productService.searchProductByKeywords("%"+keyword+"%");
        return searchedProducts;
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProducts(@RequestBody Product product, @PathVariable Long productId){
        ProductDTO productDTO = productService.updateProducts(product, productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
