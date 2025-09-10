package com.ecommerce_pineaster.pi_eco.controller;

import com.ecommerce_pineaster.pi_eco.payload.OrderDTO;
import com.ecommerce_pineaster.pi_eco.payload.OrderRequestDTO;
import com.ecommerce_pineaster.pi_eco.service.OrderService;
import com.ecommerce_pineaster.pi_eco.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/users/payment/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProduct(@RequestBody OrderRequestDTO orderRequestDTO,
                                                @PathVariable String paymentMethod){
        String email = authUtil.loggedInUserEmail();
        OrderDTO orderDTO = orderService.placeOrder(
                                                email,
                                                orderRequestDTO.getAddressId(),
                                                paymentMethod,
                                                orderRequestDTO.getPgName(),
                                                orderRequestDTO.getPgPaymentId(),
                                                orderRequestDTO.getStatus(),
                                                orderRequestDTO.getPgResponseMessage()
                                        );

        return new  ResponseEntity<>(orderDTO,HttpStatus.OK);

    }
}
