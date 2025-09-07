package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.payload.CartDTO;
import com.ecommerce_pineaster.pi_eco.repository.CartRepository;

import java.util.List;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCart();

    CartDTO getCart(String emailId, Long cartId);
}
