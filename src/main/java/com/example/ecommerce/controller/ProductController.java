package com.example.ecommerce.controller;


import com.example.ecommerce.repository.ProductRepository;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponseDTO;
import com.example.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api")
public class ProductController {

    final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                @Valid @RequestBody  ProductDTO productDTO){
        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
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
    public ResponseEntity<ProductDTO> updateProducts(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO savedProductDTO = productService.updateProducts(productDTO, productId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/product/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,@RequestParam("image") MultipartFile file) throws IOException{
        ProductDTO productDTO = productService.updateProductImage(productId, file);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
