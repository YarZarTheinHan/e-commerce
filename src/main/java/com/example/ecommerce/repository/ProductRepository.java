package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
 
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findByCategoryOrderByPriceAsc(Category category);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword,Pageable pageDetail);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    Page<Product> findByCategory(Category category, Pageable pageDetail);

}
