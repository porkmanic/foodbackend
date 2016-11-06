package com.intelli5.back.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.intelli5.back.domain.FoodJoint;
import com.intelli5.back.service.FoodJointService;
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

/**
 * REST controller for managing FoodJoint.
 */
@RestController
@RequestMapping("/api")
public class FoodJointResource {

    private final Logger log = LoggerFactory.getLogger(FoodJointResource.class);
        
    @Inject
    private FoodJointService foodJointService;

    /**
     * POST  /food-joints : Create a new foodJoint.
     *
     * @param foodJoint the foodJoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new foodJoint, or with status 400 (Bad Request) if the foodJoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/food-joints")
    @Timed
    public ResponseEntity<FoodJoint> createFoodJoint(@RequestBody FoodJoint foodJoint) throws URISyntaxException {
        log.debug("REST request to save FoodJoint : {}", foodJoint);
        if (foodJoint.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("foodJoint", "idexists", "A new foodJoint cannot already have an ID")).body(null);
        }
        FoodJoint result = foodJointService.save(foodJoint);
        return ResponseEntity.created(new URI("/api/food-joints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("foodJoint", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /food-joints : Updates an existing foodJoint.
     *
     * @param foodJoint the foodJoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated foodJoint,
     * or with status 400 (Bad Request) if the foodJoint is not valid,
     * or with status 500 (Internal Server Error) if the foodJoint couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/food-joints")
    @Timed
    public ResponseEntity<FoodJoint> updateFoodJoint(@RequestBody FoodJoint foodJoint) throws URISyntaxException {
        log.debug("REST request to update FoodJoint : {}", foodJoint);
        if (foodJoint.getId() == null) {
            return createFoodJoint(foodJoint);
        }
        FoodJoint result = foodJointService.save(foodJoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("foodJoint", foodJoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /food-joints : get all the foodJoints.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of foodJoints in body
     */
    @GetMapping("/food-joints")
    @Timed
    public List<FoodJoint> getAllFoodJoints() {
        log.debug("REST request to get all FoodJoints");
        return foodJointService.findAll();
    }

    /**
     * GET  /food-joints/:id : get the "id" foodJoint.
     *
     * @param id the id of the foodJoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the foodJoint, or with status 404 (Not Found)
     */
    @GetMapping("/food-joints/{id}")
    @Timed
    public ResponseEntity<FoodJoint> getFoodJoint(@PathVariable Long id) {
        log.debug("REST request to get FoodJoint : {}", id);
        FoodJoint foodJoint = foodJointService.findOne(id);
        return Optional.ofNullable(foodJoint)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /food-joints/:id : delete the "id" foodJoint.
     *
     * @param id the id of the foodJoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/food-joints/{id}")
    @Timed
    public ResponseEntity<Void> deleteFoodJoint(@PathVariable Long id) {
        log.debug("REST request to delete FoodJoint : {}", id);
        foodJointService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("foodJoint", id.toString())).build();
    }

}
