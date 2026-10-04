package com.example.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@ToString 
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank 
    @Size(min = 3)
    private String productName;

    private String image;

    @Size(min = 6)
    private String description;

    private Integer quantity;

    private Double price;

    private Double discount;

    private Double specialPrice;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ToString.Exclude
    @ManyToOne 
    @JoinColumn (name = "seller_id")
    private User user;

}
