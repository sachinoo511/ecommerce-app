package com.ecommerce_pineaster.pi_eco.repository;


import com.ecommerce_pineaster.pi_eco.model.Cart;
import com.ecommerce_pineaster.pi_eco.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);

    List<Cart> findCartsByProductId(Long productId);
}
