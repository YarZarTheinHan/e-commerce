package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
