package com.ecommerce_pineaster.pi_eco.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItemDTOList;
    private LocalDateTime orderDate;
    private PaymentDTO paymentDTO;
    private Double totalPrice;
    private String orderStatus;
    private Long addressId;

}
