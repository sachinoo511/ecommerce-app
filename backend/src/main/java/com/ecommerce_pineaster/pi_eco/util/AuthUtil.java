package com.ecommerce_pineaster.pi_eco.util;

import com.ecommerce_pineaster.pi_eco.model.User;
import com.ecommerce_pineaster.pi_eco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    public String loggedInUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  userRepository.findByUsername(authentication.getName())
                .orElseThrow( () -> new UsernameNotFoundException("Username not found"));

        return user.getEmail();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow( () -> new UsernameNotFoundException("Username not found"));
        return user;
    }

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow( () -> new UsernameNotFoundException("Username not found"));
        return user.getUserId();
    }
}
