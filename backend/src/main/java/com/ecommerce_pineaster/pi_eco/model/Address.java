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
    @Size(min = 10, message = "At least 5 characters required")
    private String street;

    @NotBlank
    @Size(min = 5, message = "At least 5 characters required")
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
    @ManyToMany(mappedBy = "addresses")
    private List<User> users  = new ArrayList<>();



    public Address(String street, String buildingName, String city, String state, String country, String pincode, List<User> users) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.users = users;
    }
}
