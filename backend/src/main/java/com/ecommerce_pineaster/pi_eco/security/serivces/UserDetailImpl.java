package com.ecommerce_pineaster.pi_eco.security.serivces;

import com.ecommerce_pineaster.pi_eco.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserDetailImpl  implements UserDetails {

    private  static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;



    public static UserDetailImpl build(User user){
        List<GrantedAuthority>authorityList = user.getRoleSet().stream().
                map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).
                collect(Collectors.toList());
        return new UserDetailImpl(
                user.getUserId(),
                user.getUsername(), user.getEmail(),user.getPassword(),
                authorityList
        );


    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}
