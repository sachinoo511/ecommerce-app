package com.ecommerce_pineaster.pi_eco.repository;

import com.ecommerce_pineaster.pi_eco.model.AppRole;
import com.ecommerce_pineaster.pi_eco.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface   RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
