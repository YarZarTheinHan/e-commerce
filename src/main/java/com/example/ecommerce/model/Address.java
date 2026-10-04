package com.example.ecommerce.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

@Entity 
@ToString 
@Table(name = "addresses")
public class Address {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank 
    @Size(min = 5, max = 255)
    private String street;

    @NotBlank 
    @Size(min = 5, max = 255)
    private String state;

    @NotBlank 
    @Size(min = 5, max = 155)
    private String building;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users;

}
