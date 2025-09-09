package com.ecommerce_pineaster.pi_eco.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private  Long addressId;
    private String paymentMethod;
    private String pgName;
    private String pgPaymentId;
    private String pgResponseMessage;
    private String status;
 }
