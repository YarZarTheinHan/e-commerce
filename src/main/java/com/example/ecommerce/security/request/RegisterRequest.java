package com.example.ecommerce.security.request;

import java.util.Set;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class RegisterRequest {
    @NotBlank 
    @Size(min = 5, max = 30)
    private String username;

    @NotBlank 
    @Email 
    @Size(min = 5, max = 50)
    private String email;

    @NotBlank 
    @Size(min = 6, max = 40)
    private String password;

    private Set<String> role;
}
