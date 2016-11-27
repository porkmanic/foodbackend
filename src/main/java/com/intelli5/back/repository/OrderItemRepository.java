package com.intelli5.back.repository;

import com.intelli5.back.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderItem entity.
 */
@SuppressWarnings("unused")
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByFoodOrder_Id(Long foodOrderId);
}
