package com.ecommerce_pineaster.pi_eco.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer cartQuantity;
    private String image;
    private double price;
    private double discount;
    private double specialPrice;
}
