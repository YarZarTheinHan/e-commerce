package com.example.ecommerce.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data 
@ToString 
@NoArgsConstructor 
@Table(name = "users", uniqueConstraints = {@UniqueConstraint (columnNames = "user_name"),
                        @UniqueConstraint(columnNames="email")}
)
public class User {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    @NotBlank 
    @Size(max=30)
    @Column(name = "user_name")
    private String userName;

    @NotBlank 
    @Size(max = 30)
    @Email
    @Column(name = "email") 
    private String email;

    @NotBlank 
    @Size(min = 6, max = 120)
    @Column(name = "password")
    private String password;

    @Getter 
    @Setter 
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns= @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
    orphanRemoval = true)
    private Set<Product> products;

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_address",
        joinColumns =@JoinColumn (name="user_id"),
        inverseJoinColumns = @JoinColumn (name="addressId")
        
    )
    private List<Address> addresses = new ArrayList<>();

}
