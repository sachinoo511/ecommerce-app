package com.ecommerce_pineaster.pi_eco.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 1, message = "At least 5 characters required")
    private String street;

    @NotBlank
    @Size(min = 2, message = "At least 5 characters required")
    private String buildingName;

    @NotBlank
    @Size(min = 3, message = "At least 3 characters required")
    private String city;

    @NotBlank
    @Size(min = 3, message = "At least 3 characters required")
    private String state;
    @NotBlank
    @Size(min = 3, message = "At least 3 characters required")
    private String country;

    @NotBlank
    @Size(min = 3, message = "At least 3 characters required")
    private String pincode;


    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
