package com.intelli5.back.repository;

import com.intelli5.back.domain.FoodOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FoodOrder entity.
 */
@SuppressWarnings("unused")
public interface FoodOrderRepository extends JpaRepository<FoodOrder,Long> {

}
