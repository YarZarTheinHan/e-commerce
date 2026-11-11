package com.example.ecommerce.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.example.ecommerce.security.jwt.JWTUtils;
import com.example.ecommerce.security.request.LoginRequest;
import com.example.ecommerce.security.request.RegisterRequest;
import com.example.ecommerce.security.response.MessageResponse;
import com.example.ecommerce.security.response.UserInfoResponse;
import com.example.ecommerce.security.services.CustomUserDetails;
import com.example.ecommerce.model.AppRole;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    private final UserRepository userRepo;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;

    AuthController(AuthenticationManager authenticationManager, JWTUtils jwtUtils, UserRepository userRepo, 
        PasswordEncoder encoder, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
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

    ResponseCookie jwtTokenCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    UserInfoResponse responseBody = new UserInfoResponse(
            userDetails.getUserId(),
            userDetails.getUsername(),
            roles
    );

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
        jwtTokenCookie.toString()).body(responseBody);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        if(userRepo.existsByUserName(registerRequest.getUsername())){
           return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userRepo.existsByEmail(registerRequest.getEmail())){
           return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already registered!"));
        }

        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role->{
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(()-> new RuntimeException("Error: Role is not found!"));
                        roles.add(adminRole);
                        break;

                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                        .orElseThrow(()-> new RuntimeException("Error: Role is not found!"));
                        roles.add(sellerRole);
                        break;
                
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseThrow(()-> new RuntimeException("Error: Role is not found!"));
                        roles.add(userRole);
                        break;
                }
            });
        }
        user.setRoles(roles);
        userRepo.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/username")
    public String getUserName(Authentication authentication) {
        if(authentication != null){
            return authentication.getName();
        }
        return "NULL";
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = customUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    UserInfoResponse responseBody = new UserInfoResponse(
            customUserDetails.getUserId(),
            customUserDetails.getUsername(),
            roles
    );
     return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(){
        ResponseCookie cookie = jwtUtils.clearJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("Successfully Logged Out!"));
    }
}
    
