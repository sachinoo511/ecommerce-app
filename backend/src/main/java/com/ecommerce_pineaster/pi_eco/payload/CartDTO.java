package com.ecommerce_pineaster.pi_eco.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long cartId;
    private Double totalprice = 0.0;
    private List<ProductDTO> products = new ArrayList<>();

}
