package com.ecommerce_pineaster.pi_eco.repository;

import com.ecommerce_pineaster.pi_eco.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemsRepository  extends JpaRepository<CartItem,Long> {


    @Query("SELECT ci FROM CartItem ci where  ci.cart.cartId = ?1 and ci.product.productId = ?2")
    CartItem findCartItemByProductIdAndCartId( Long cartItemsId, Long productId);

}
