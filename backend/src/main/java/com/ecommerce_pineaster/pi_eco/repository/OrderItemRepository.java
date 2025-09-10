package com.ecommerce_pineaster.pi_eco.repository;

import com.ecommerce_pineaster.pi_eco.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
