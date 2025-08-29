package com.ecommerce_pineaster.pi_eco.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO {

    private Long categoryId;
    private String categoryName;

}