package com.ecommerce_pineaster.pi_eco.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    private Long userId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max=40)
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank
    @Size(max=120)
    @Column(name = "password")
    private String password;


    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
     fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roleSet = new HashSet<>();




   @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},
    orphanRemoval = true)
    private Set<Product> products;


   @ToString.Exclude
   @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
   private Cart cart;


    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "user_address",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<Address> addresses = new ArrayList<>();



    public User(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
    }




}
