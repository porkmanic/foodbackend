package com.intelli5.back.repository;

import com.intelli5.back.domain.FoodJoint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FoodJoint entity.
 */
@SuppressWarnings("unused")
public interface FoodJointRepository extends JpaRepository<FoodJoint,Long> {

}
