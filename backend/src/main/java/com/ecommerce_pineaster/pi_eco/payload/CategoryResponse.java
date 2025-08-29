package com.ecommerce_pineaster.pi_eco.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryResponse {

    private List<CategoryDTO> content;
    private Integer pageNumber;
    private  Integer pageSize;
    private  Long totalElements;
    private  Integer totalPages;
    private  boolean lastPage;
}


