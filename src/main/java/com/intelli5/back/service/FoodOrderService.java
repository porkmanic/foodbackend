package com.intelli5.back.service;

import com.intelli5.back.domain.FoodOrder;
import com.intelli5.back.repository.FoodOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing FoodOrder.
 */
@Service
@Transactional
public class FoodOrderService {

    private final Logger log = LoggerFactory.getLogger(FoodOrderService.class);
    
    @Inject
    private FoodOrderRepository foodOrderRepository;

    /**
     * Save a foodOrder.
     *
     * @param foodOrder the entity to save
     * @return the persisted entity
     */
    public FoodOrder save(FoodOrder foodOrder) {
        log.debug("Request to save FoodOrder : {}", foodOrder);
        FoodOrder result = foodOrderRepository.save(foodOrder);
        return result;
    }

    /**
     *  Get all the foodOrders.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<FoodOrder> findAll() {
        log.debug("Request to get all FoodOrders");
        List<FoodOrder> result = foodOrderRepository.findAll();

        return result;
    }


    /**
     *  get all the foodOrders where Ticket is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<FoodOrder> findAllWhereTicketIsNull() {
        log.debug("Request to get all foodOrders where Ticket is null");
        return StreamSupport
            .stream(foodOrderRepository.findAll().spliterator(), false)
            .filter(foodOrder -> foodOrder.getTicket() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one foodOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FoodOrder findOne(Long id) {
        log.debug("Request to get FoodOrder : {}", id);
        FoodOrder foodOrder = foodOrderRepository.findOne(id);
        return foodOrder;
    }

    /**
     *  Delete the  foodOrder by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FoodOrder : {}", id);
        foodOrderRepository.delete(id);
    }
}
