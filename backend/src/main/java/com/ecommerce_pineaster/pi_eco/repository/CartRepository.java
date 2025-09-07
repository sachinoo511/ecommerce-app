package com.ecommerce_pineaster.pi_eco.repository;

import com.ecommerce_pineaster.pi_eco.model.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query("select c from Cart c where c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("select c from Cart c where  c.user.email = ?1 and c.cartId = ?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);
}
