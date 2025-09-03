package com.ecommerce_pineaster.pi_eco.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {

    private Long id;

    private String jwtToken;

    private String userName;

    private List<String> roles;



}
