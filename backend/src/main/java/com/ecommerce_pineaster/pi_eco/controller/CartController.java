package com.ecommerce_pineaster.pi_eco.controller;

import com.ecommerce_pineaster.pi_eco.model.Cart;
import com.ecommerce_pineaster.pi_eco.payload.CartDTO;
import com.ecommerce_pineaster.pi_eco.payload.CartItemsDTO;
import com.ecommerce_pineaster.pi_eco.repository.CartRepository;
import com.ecommerce_pineaster.pi_eco.service.CartService;
import com.ecommerce_pineaster.pi_eco.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,@PathVariable Integer quantity) {

        CartDTO cartDTO =  cartService.addProductToCart(productId,quantity);
        return new  ResponseEntity<CartDTO>(cartDTO,HttpStatus.CREATED);

    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCart(){

        List<CartDTO> cartDTOs =  cartService.getAllCart();

        return  new ResponseEntity<>(cartDTOs,HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    public  ResponseEntity<CartDTO>  getCartById(){
        String emailId  = authUtil.loggedInUserEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO =  cartService.getCart(emailId,cartId);

        return  new ResponseEntity<>(cartDTO,HttpStatus.OK);

    }
}
