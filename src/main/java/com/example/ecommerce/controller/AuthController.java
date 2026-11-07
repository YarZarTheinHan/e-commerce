package com.example.ecommerce.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce.security.jwt.JWTUtils;
import com.example.ecommerce.security.request.LoginRequest;
import com.example.ecommerce.security.request.RegisterRequest;
import com.example.ecommerce.security.response.MessageResponse;
import com.example.ecommerce.security.response.UserInfoResponse;
import com.example.ecommerce.security.services.CustomUserDetails;
import com.example.ecommerce.repository.UserRepository;

import jakarta.validation.Valid;

public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    private final UserRepository userRepo;

    AuthController(AuthenticationManager authenticationManager, JWTUtils jwtUtils, UserRepository userRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), 
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Cast explicitly to CustomUserDetails
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    UserInfoResponse responseBody = new UserInfoResponse(
            userDetails.getUserId(),
            jwtToken,
            userDetails.getUsername(),
            roles
    );

    return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        if(userRepo.existsByUserName(registerRequest.getUsername())){
           return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userRepo.existsByEmail(registerRequest.getEmail())){
           return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already registered!"));
        }
        return null;
    }
}
