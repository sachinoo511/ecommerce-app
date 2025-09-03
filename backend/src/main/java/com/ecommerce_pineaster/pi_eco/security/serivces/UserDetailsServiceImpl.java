package com.ecommerce_pineaster.pi_eco.security.serivces;

import com.ecommerce_pineaster.pi_eco.model.User;
import com.ecommerce_pineaster.pi_eco.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->  new UsernameNotFoundException("User Not Found"));

        return UserDetailImpl.build(user);
    }
}
