package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCart();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateCartProductQuantity(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}
