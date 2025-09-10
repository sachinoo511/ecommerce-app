package com.ecommerce_pineaster.pi_eco.repository;

import com.ecommerce_pineaster.pi_eco.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemsRepository  extends JpaRepository<CartItem,Long> {


    @Query("SELECT ci FROM CartItem ci where  ci.cart.cartId = ?1 and ci.product.productId = ?2")
    CartItem findCartItemByProductIdAndCartId( Long cartId, Long productId);

    @Modifying
    @Query("delete from CartItem ci where ci.cart.cartId=?1 and ci.product.productId=?2")
    void deleteCartItemByProdcutIdAndCardId(Long cartId, Long productId);

}
