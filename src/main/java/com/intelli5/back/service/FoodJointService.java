package com.intelli5.back.service;

import com.intelli5.back.domain.FoodJoint;
import com.intelli5.back.repository.FoodJointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing FoodJoint.
 */
@Service
@Transactional
public class FoodJointService {

    private final Logger log = LoggerFactory.getLogger(FoodJointService.class);
    
    @Inject
    private FoodJointRepository foodJointRepository;

    /**
     * Save a foodJoint.
     *
     * @param foodJoint the entity to save
     * @return the persisted entity
     */
    public FoodJoint save(FoodJoint foodJoint) {
        log.debug("Request to save FoodJoint : {}", foodJoint);
        FoodJoint result = foodJointRepository.save(foodJoint);
        return result;
    }

    /**
     *  Get all the foodJoints.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<FoodJoint> findAll() {
        log.debug("Request to get all FoodJoints");
        List<FoodJoint> result = foodJointRepository.findAll();

        return result;
    }

    /**
     *  Get one foodJoint by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FoodJoint findOne(Long id) {
        log.debug("Request to get FoodJoint : {}", id);
        FoodJoint foodJoint = foodJointRepository.findOne(id);
        return foodJoint;
    }

    /**
     *  Delete the  foodJoint by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FoodJoint : {}", id);
        foodJointRepository.delete(id);
    }
}
