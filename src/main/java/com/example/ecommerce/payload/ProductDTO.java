package com.example.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductDTO {
    private Long productId;

    @NotBlank (message = "Product Name must contain!")
    @Size(min=3, message = "Product Name must contain at least 3 words!")
    private String productName;

    private String image;

    @Size(min=6, message = "Description must contain at least 6 words!")
    private String description;

    private Integer quantity;

    private Double price;

    private Double discount;

    private Double specialPrice;
}
