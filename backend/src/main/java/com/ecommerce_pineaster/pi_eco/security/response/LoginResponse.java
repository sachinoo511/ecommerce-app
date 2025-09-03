package com.ecommerce_pineaster.pi_eco.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.List;

@NoArgsConstructor
@Data
public class LoginResponse {

    private Long id;

    private  ResponseCookie jwtCookie;

    private String userName;

    private List<String> roles;


    public LoginResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.userName = username;
        this.roles = roles;
    }

    public LoginResponse(Long id, ResponseCookie jwtCookie, String username, List<String> roles) {
        this.id = id;
        this.userName = username;
        this.roles = roles;
        this.jwtCookie = jwtCookie;
    }
}
