package com.ecommerce_pineaster.pi_eco.controller;


import com.ecommerce_pineaster.pi_eco.model.AppRole;
import com.ecommerce_pineaster.pi_eco.model.Role;
import com.ecommerce_pineaster.pi_eco.model.User;
import com.ecommerce_pineaster.pi_eco.payload.MessageResponse.MessageResponse;
import com.ecommerce_pineaster.pi_eco.repository.RoleRepository;
import com.ecommerce_pineaster.pi_eco.repository.UserRepository;
import com.ecommerce_pineaster.pi_eco.security.request.LoginRequest;
import com.ecommerce_pineaster.pi_eco.security.request.SignUpRequest;
import com.ecommerce_pineaster.pi_eco.security.response.LoginResponse;
import com.ecommerce_pineaster.pi_eco.security.jwt.JwtUtils;
import com.ecommerce_pineaster.pi_eco.security.serivces.UserDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest){
        Authentication authentication;

        try {
        authentication  = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
        );

        } catch (AuthenticationException authenticationException){

            Map<String,Object> map = new HashMap<>();
            map.put("message","Bad Credentials");
            map.put("status",false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie =  jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item ->item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse(userDetails.getId(),jwtCookie,userDetails.getUsername(),roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(loginResponse);


    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){
        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return  ResponseEntity.badRequest().body( new MessageResponse("Error: Username is already taken "));

        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken "));
        }

        User user =  new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles  = new HashSet<>();

        if (strRoles ==null){
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                            .orElseThrow(()-> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        }else{

            strRoles.forEach(role->{
                switch(role){
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                        .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                        break;
                   case "seller":
                       Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                       .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                       roles.add(sellerRole);
                       break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(userRole);

                }
            }

            );
        }

        user.setRoleSet(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }


    @GetMapping("/username")
    public String getCurrentUserName( Authentication authentication){
        if(authentication!=null){
            return authentication.getName();
        }else {
            return null;
        }
    }

    @GetMapping("/userdetails")
    public String getCurrentUserDetails(Authentication authentication){
        UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
        List<String> role = userDetails.getAuthorities().stream().
                map(item-> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse(userDetails.getId(),userDetails.getUsername(),role);
        return loginResponse.toString();
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return  ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                cookie.toString()).body(new MessageResponse("You've been logged out successfully"));

    }


}
