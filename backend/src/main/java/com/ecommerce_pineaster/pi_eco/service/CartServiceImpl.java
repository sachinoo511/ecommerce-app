package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.exception.ApiException;
import com.ecommerce_pineaster.pi_eco.exception.ResourceNotFoundException;
import com.ecommerce_pineaster.pi_eco.model.Cart;
import com.ecommerce_pineaster.pi_eco.model.CartItem;
import com.ecommerce_pineaster.pi_eco.model.Product;
import com.ecommerce_pineaster.pi_eco.payload.CartDTO;
import com.ecommerce_pineaster.pi_eco.payload.ProductDTO;
import com.ecommerce_pineaster.pi_eco.repository.CartItemsRepository;
import com.ecommerce_pineaster.pi_eco.repository.CartRepository;
import com.ecommerce_pineaster.pi_eco.repository.ProductRepository;
import com.ecommerce_pineaster.pi_eco.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements  CartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemsRepository  cartItemsRepository;




    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
    // Find existing card or create one
        Cart cart  = createCart();
     // Retrieve Product Details
        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product","Prodcut id",productId));
// Add Validation
        CartItem cartItems = cartItemsRepository
                .findCartItemByProductIdAndCartId(cart.getCartId(),productId);

        if(cartItems!=null){
            throw  new ApiException("Product "+product.getProductName()+" already exists");
        }

        if(product.getQuantity()<quantity){

            throw  new ApiException("please , make an oder  of the"+product.getProductName()+"  less than or equal to the " +
                    "quantity " +product.getQuantity()+".");
        }

        if(product.getQuantity()==0){
            throw new ApiException(product.getProductImage()+" is not available.");
        }

        // Create cartItem
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);
        cartItem.setDiscount(product.getDiscount());
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItemsRepository.save(cartItem);

        //Update Cart
        product.setQuantity(product.getQuantity()-quantity);

        cart.setTotalPrice(cart.getTotalPrice()+product.getSpecialPrice()*quantity);
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItem>   cartItemList = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItemList.stream().map(
                cartItem1 -> {
                    ProductDTO productDTO = modelMapper.map(cartItem1.getProduct(),ProductDTO.class);
                    cartItem1.setQuantity(cartItem1.getQuantity());
                    return productDTO;
                }

        );

        cartDTO.setProducts(productDTOStream.toList());



        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCart() {
        List<Cart> carts = (List<Cart>) cartRepository.findAll();
        if(carts.isEmpty()){
            throw new ApiException("No carts found.");
        }

        List<CartDTO> cartDTO = carts.stream().map(cart -> {
            CartDTO cartDTO1 = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> products =  cart.getCartItems().stream()
                    .map(cartItem ->
                        modelMapper.map(cartItem.getProduct(),ProductDTO.class)
                    ).collect(Collectors.toList());
            cartDTO1.setProducts(products);

            return cartDTO1;
        }).collect(Collectors.toList());

//        List<CartDTO> cartDTOList = new ArrayList<>();
//        for(Cart cart :cartList){
//            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
//            cartDTOList.add(cartDTO);
//        }
        return cartDTO;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
    Cart cart = cartRepository.findCartByEmailAndCartId(emailId,cartId);
    CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
    cart.getCartItems().forEach(cartItem -> cartItem.setQuantity(cartItem.getQuantity()));
    List<ProductDTO> productDTOS = cart.getCartItems().stream()
            .map(cartItem -> modelMapper.map(cartItem.getProduct(),ProductDTO.class)).collect(Collectors.toList());
    cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    public Cart createCart(){

        Cart cart = cartRepository.findCartByEmail(authUtil.loggedInUserEmail());
        if(cart!=null){
            return cart;
        }
        Cart newCart = new Cart();
        newCart.setTotalPrice(0.00);
        newCart.setUser(authUtil.loggedInUser());
        Cart savedCart = cartRepository.save(newCart);
        return savedCart;
    }
}
