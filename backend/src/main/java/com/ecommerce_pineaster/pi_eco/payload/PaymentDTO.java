package com.ecommerce_pineaster.pi_eco.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private String paymentMethod;
    private String pgStatus;
    private String pgPaymentId;
    private String pgPaymentMessage;
    private String pgResponseMessage;
    private String pgName;

}
