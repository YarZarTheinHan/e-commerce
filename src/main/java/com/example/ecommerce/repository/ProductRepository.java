package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductResponseDTO;
 
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findByCategoryOrderByPriceAsc(Category category);

}
