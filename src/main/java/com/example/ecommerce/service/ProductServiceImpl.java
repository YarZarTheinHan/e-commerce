package com.example.ecommerce.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
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

    @Override
    public ProductResponseDTO searchProductByKeywords(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase(keyword);
        List<ProductDTO> productDTOs= products.stream()
        .map(product->mapper.map(product, ProductDTO.class)).toList();

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setContent(productDTOs);
        return responseDTO;
    }

    @Override
    public ProductDTO updateProducts(Product product, Long productId) {
        //Get the product from DB
        Product productSavedDB = productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("Product","Product ID", productId));
        //Update the product that user updated (Need to handle the Special Prize)
        productSavedDB.setProductName(product.getProductName());
        productSavedDB.setDescription(product.getDescription());
        productSavedDB.setQuantity(product.getQuantity());
        productSavedDB.setPrice(product.getPrice());
        productSavedDB.setDiscount(product.getDiscount());
        double specialPrice = product.getPrice()-((product.getDiscount() * 0.01) * product.getPrice());
        productSavedDB.setSpecialPrice(specialPrice);

        //Save to DB
        productRepository.save(productSavedDB);
        return mapper.map(productSavedDB,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
       Product product = productRepository.findById(productId)
       .orElseThrow(()-> new ResourceNotFoundException("Product","Product Id", productId));
       productRepository.delete(product);
       return mapper.map(product, ProductDTO.class);
    }
}
