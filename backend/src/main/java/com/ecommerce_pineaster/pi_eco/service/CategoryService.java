package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.model.Category;
import com.ecommerce_pineaster.pi_eco.payload.CategoryDTO;
import com.ecommerce_pineaster.pi_eco.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id);
}
