package com.intelli5.back.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.intelli5.back.domain.FoodOrder;
import com.intelli5.back.service.FoodOrderService;
import com.intelli5.back.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing FoodOrder.
 */
@RestController
@RequestMapping("/api")
public class FoodOrderResource {

    private final Logger log = LoggerFactory.getLogger(FoodOrderResource.class);
        
    @Inject
    private FoodOrderService foodOrderService;

    /**
     * POST  /food-orders : Create a new foodOrder.
     *
     * @param foodOrder the foodOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new foodOrder, or with status 400 (Bad Request) if the foodOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/food-orders")
    @Timed
    public ResponseEntity<FoodOrder> createFoodOrder(@RequestBody FoodOrder foodOrder) throws URISyntaxException {
        log.debug("REST request to save FoodOrder : {}", foodOrder);
        if (foodOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("foodOrder", "idexists", "A new foodOrder cannot already have an ID")).body(null);
        }
        FoodOrder result = foodOrderService.save(foodOrder);
        return ResponseEntity.created(new URI("/api/food-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("foodOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /food-orders : Updates an existing foodOrder.
     *
     * @param foodOrder the foodOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated foodOrder,
     * or with status 400 (Bad Request) if the foodOrder is not valid,
     * or with status 500 (Internal Server Error) if the foodOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/food-orders")
    @Timed
    public ResponseEntity<FoodOrder> updateFoodOrder(@RequestBody FoodOrder foodOrder) throws URISyntaxException {
        log.debug("REST request to update FoodOrder : {}", foodOrder);
        if (foodOrder.getId() == null) {
            return createFoodOrder(foodOrder);
        }
        FoodOrder result = foodOrderService.save(foodOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("foodOrder", foodOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /food-orders : get all the foodOrders.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of foodOrders in body
     */
    @GetMapping("/food-orders")
    @Timed
    public List<FoodOrder> getAllFoodOrders(@RequestParam(required = false) String filter) {
        if ("ticket-is-null".equals(filter)) {
            log.debug("REST request to get all FoodOrders where ticket is null");
            return foodOrderService.findAllWhereTicketIsNull();
        }
        log.debug("REST request to get all FoodOrders");
        return foodOrderService.findAll();
    }

    /**
     * GET  /food-orders/:id : get the "id" foodOrder.
     *
     * @param id the id of the foodOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the foodOrder, or with status 404 (Not Found)
     */
    @GetMapping("/food-orders/{id}")
    @Timed
    public ResponseEntity<FoodOrder> getFoodOrder(@PathVariable Long id) {
        log.debug("REST request to get FoodOrder : {}", id);
        FoodOrder foodOrder = foodOrderService.findOne(id);
        return Optional.ofNullable(foodOrder)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /food-orders/:id : delete the "id" foodOrder.
     *
     * @param id the id of the foodOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/food-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteFoodOrder(@PathVariable Long id) {
        log.debug("REST request to delete FoodOrder : {}", id);
        foodOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("foodOrder", id.toString())).build();
    }

}
