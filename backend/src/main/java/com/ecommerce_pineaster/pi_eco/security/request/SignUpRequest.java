package com.ecommerce_pineaster.pi_eco.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 30)
    private String username;
    @NotBlank
    @Size(min = 2, max = 50)
    @Email
    private String email;

    @Size(max = 120)
    @NotBlank
    private String password;

    private Set<String> roles;

}
