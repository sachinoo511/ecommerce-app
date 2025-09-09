package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.exception.ApiException;
import com.ecommerce_pineaster.pi_eco.exception.ResourceNotFoundException;
import com.ecommerce_pineaster.pi_eco.model.*;
import com.ecommerce_pineaster.pi_eco.payload.OrderDTO;
import com.ecommerce_pineaster.pi_eco.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public OrderDTO placeOrder(String email, Long addressId,  String paymentMethod, String pgName,
                               String pgPaymentId, String pgStatus,
                               String pgResponseMessage) {
        //Get User Cart
        Cart cart  = cartRepository.findCartByEmail(email);
        if(cart==null){
            throw  new ResourceNotFoundException("Cart","email",email);
        }

        Address address =  addressRepository.findById(addressId)
                .orElseThrow( () -> new ResourceNotFoundException("Address","addressId",addressId));

        // Create New Order with Payment Info
        Order order = new Order();
        order.setEmail(email);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!");

        Payment payment = new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        payment =paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        //Get the Item from Cart  into  the order Items
        List<CartItem> cartItemList = cart.getCartItems();
        if(cartItemList==null){
            throw new ApiException("Cart is Empty");

        }

        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
             OrderItem orderItem = new OrderItem();
             orderItem.setProduct(cartItem.getProduct());
             orderItem.setQuantity(cartItem.getQuantity());
             orderItem.setDiscount(cartItem.getDiscount());
             orderItem.setOrderedProductPrice(cartItem.getProductPrice());
             orderItem.setOrder(order);
             orderItemList.add(orderItem);
        }

        orderItemList = orderItemRepository.saveAll(orderItemList);


        //Update Product Stock
        cart.getCartItems().forEach(item->{
            int quantity = item.getQuantity();
            item.setQuantity(item.getProduct().getQuantity()-quantity);
            productRepository.save(item.getProduct());
        });


        return null;
    }
}
