package com.ecommerce_pineaster.pi_eco.security;

import com.ecommerce_pineaster.pi_eco.model.AppRole;
import com.ecommerce_pineaster.pi_eco.model.Role;
import com.ecommerce_pineaster.pi_eco.model.User;
import com.ecommerce_pineaster.pi_eco.repository.RoleRepository;
import com.ecommerce_pineaster.pi_eco.repository.UserRepository;
import com.ecommerce_pineaster.pi_eco.security.jwt.AuthEntryPointJwt;
import com.ecommerce_pineaster.pi_eco.security.jwt.AuthTokenFilter;
import com.ecommerce_pineaster.pi_eco.security.serivces.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authTokenJwtFilter(){
        return  new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return  provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return  authConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return  new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/api/public/**").permitAll()
//                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers("/v3/api-doc",
                "/configuration/ui",
                "/swagger-resource/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
                )
        );
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,PasswordEncoder passwordEncoder){

        return args -> {
            //Retrieve and Create Roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });


            Role sellerRole  = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(()->{
                        Role newSeller = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSeller);
                    });

            Role adminRole  = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(()->{
                        Role newAdmin = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdmin);
                    });


            Set<Role> userRoles =  Set.of(userRole);
            Set<Role> sellerRoles =  Set.of(sellerRole);
            Set<Role> adminRoles =  Set.of(userRole,sellerRole,adminRole);


            //Create User If not already present

            if(!userRepository.existsByUsername("user1")){
                User user1 = new User("user1","user@gmail.com"
                        ,passwordEncoder.encode("123456"));
//                user1.setRoleSet(userRoles);
                userRepository.save(user1);
            }

            if(!userRepository.existsByUsername("user2")){
                User seller = new User("user2","userSeller@gmail.com"
                        ,passwordEncoder.encode("112426"));
//                seller.setRoleSet(userRoles);
                userRepository.save(seller);
            }

            if(!userRepository.existsByUsername("admin")){
                User admin = new User("admin","admin@gmail.com"
                        ,passwordEncoder.encode("112426"));
//                admin.setRoleSet(userRoles);
                userRepository.save(admin);
            }

            //update Role of userName
            userRepository.findByUsername("user1").ifPresent(user->{
                user.setRoleSet(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("user2").ifPresent(seller->{
                seller.setRoleSet(sellerRoles);
                userRepository.save(seller);
            });
            userRepository.findByUsername("admin").ifPresent(admin->{
                admin.setRoleSet(adminRoles);
                userRepository.save(admin);
            });


        };
    }







}
