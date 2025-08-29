package com.ecommerce_pineaster.pi_eco.repository;


import com.ecommerce_pineaster.pi_eco.model.Category;
import com.ecommerce_pineaster.pi_eco.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
        Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);
        Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageDetails);

    Product findByProductName(String productName);
}
