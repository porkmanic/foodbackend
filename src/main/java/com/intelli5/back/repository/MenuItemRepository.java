package com.intelli5.back.repository;

import com.intelli5.back.domain.MenuItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MenuItem entity.
 */
@SuppressWarnings("unused")
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
    public List<MenuItem> findByFoodJoint_Id(Long id);
}
