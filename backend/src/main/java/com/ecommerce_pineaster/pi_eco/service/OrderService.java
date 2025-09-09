package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.payload.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(String s, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String status, String pgResponseMessage);
}
