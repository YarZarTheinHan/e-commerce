package com.example.ecommerce.service;


import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.exceptions.APIException;
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

    final FileService fileService;

    @Value("${image.path}")
    String path;

    ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper mapper,
        FileService fileService
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.fileService = fileService;
    }

    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO){
       Category  categories = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category Not Found","categoryId",categoryId));
        List<Product> existedProduct = productRepository.findByProductNameLikeIgnoreCase(productDTO.getProductName());
        int size = existedProduct.size();
        if( size != 0){
            throw new APIException("Product name is already exist!");
        } 
        Product product = mapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(categories);
            double specialPrice = product.getPrice()-((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return mapper.map(savedProduct, ProductDTO.class);
    }

    public ProductResponseDTO getAllProducts(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder){
        Sort order = sortOrder.equalsIgnoreCase("acs") ?
        Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageDetail = PageRequest.of(pageNumber,pageSize,order);
        Page<Product> pageProduct = productRepository.findAll(pageDetail);

        List<Product> product = pageProduct.getContent();
        
        if(!product.isEmpty()){
            List<ProductDTO> productDTOs = product.stream().map(products -> mapper.map(products, ProductDTO.class)).toList();
            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setContent(productDTOs);
            productResponseDTO.setLastPage(pageProduct.isLast());
            productResponseDTO.setPageNumber(pageNumber);
            productResponseDTO.setPageSize(pageSize);
            productResponseDTO.setTotalElement(pageProduct.getTotalElements());
            productResponseDTO.setTotalPages(pageProduct.getTotalPages());
            return productResponseDTO;
        } throw new APIException("Products does not exist");
    }

    @Override
    public ProductResponseDTO getProductsByCategory(Long categoryId, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
       Category category = categoryRepository.findById(categoryId).
       orElseThrow( ()-> new ResourceNotFoundException("Category", "CategoryID",categoryId));

       Sort order = sortOrder.equalsIgnoreCase("acs")? 
       Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

       Pageable pageDetail = PageRequest.of(pageNumber, pageSize, order);

       Page<Product> productByCategory = productRepository.findByCategory(category,pageDetail);
       boolean isEmpty =productByCategory.isEmpty();
       if(isEmpty){
        throw new APIException("No product exist in this category!");
       } 

       //Page<Product>  products = productRepository.findAll(pageDetail);

       List<ProductDTO> productDTOs = productByCategory.stream()
                            .map(products->mapper.map(products, ProductDTO.class)).toList();
            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setContent(productDTOs);
            productResponseDTO.setPageNumber(pageNumber);
            productResponseDTO.setPageSize(pageSize);
            productResponseDTO.setLastPage(productByCategory.isLast());
            productResponseDTO.setTotalElement(productByCategory.getTotalElements());
            productResponseDTO.setTotalPages(productByCategory.getTotalPages());

            return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductByKeywords(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        Sort order = sortOrder.equalsIgnoreCase("asc")?
        Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageDetail = PageRequest.of(pageNumber, pageSize, order);
        Page<Product> products = productRepository.findByProductNameLikeIgnoreCase(keyword,pageDetail);
        boolean isEmpty = products.isEmpty();
        if(isEmpty){
            throw new APIException("Product Not Found That You Search!");
        } 
       List<ProductDTO> productDTOs= products.stream()
        .map(product->mapper.map(product, ProductDTO.class)).toList();

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setContent(productDTOs);
        responseDTO.setPageNumber(pageNumber);
        responseDTO.setPageSize(pageSize);
        responseDTO.setLastPage(products.isLast());
        responseDTO.setTotalElement(products.getTotalElements());
        responseDTO.setTotalPages(products.getTotalPages());

        return responseDTO;
    }

    @Override
    public ProductDTO updateProducts(ProductDTO productDTO, Long productId) {
        //Get the product from DB
        Product productSavedDB = productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("Product","Product ID", productId));
        List<Product> existedProducts = productRepository.findByProductNameLikeIgnoreCase(productDTO.getProductName());
        boolean isEmpty = existedProducts.isEmpty();
        if(isEmpty){
            Product product = mapper.map(productDTO, Product.class);
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
        }throw new APIException("Product name "+ productDTO.getProductName()+" already exist!");
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
       Product product = productRepository.findById(productId)
       .orElseThrow(()-> new ResourceNotFoundException("Product","Product Id", productId));
       productRepository.delete(product);
       return mapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile file) throws IOException {
        //Take the product 
        Product savedProduct = productRepository.findById(productId).orElseThrow(
            ()-> new ResourceNotFoundException("Product","Product ID", productId));
        //upload image to the project //take the name of the image uploaded
        String imageName = fileService.uploadImage(path, file);
        //save to the db
        savedProduct.setImage(imageName);
        Product updatedProduct = productRepository.save(savedProduct);
        return mapper.map(updatedProduct, ProductDTO.class);
    }
}
