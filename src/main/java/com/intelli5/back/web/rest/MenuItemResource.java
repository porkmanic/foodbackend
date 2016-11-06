package com.intelli5.back.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.intelli5.back.domain.MenuItem;
import com.intelli5.back.service.MenuItemService;
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
 * REST controller for managing MenuItem.
 */
@RestController
@RequestMapping("/api")
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);
        
    @Inject
    private MenuItemService menuItemService;

    /**
     * POST  /menu-items : Create a new menuItem.
     *
     * @param menuItem the menuItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new menuItem, or with status 400 (Bad Request) if the menuItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/menu-items")
    @Timed
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("menuItem", "idexists", "A new menuItem cannot already have an ID")).body(null);
        }
        MenuItem result = menuItemService.save(menuItem);
        return ResponseEntity.created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("menuItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /menu-items : Updates an existing menuItem.
     *
     * @param menuItem the menuItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated menuItem,
     * or with status 400 (Bad Request) if the menuItem is not valid,
     * or with status 500 (Internal Server Error) if the menuItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/menu-items")
    @Timed
    public ResponseEntity<MenuItem> updateMenuItem(@RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}", menuItem);
        if (menuItem.getId() == null) {
            return createMenuItem(menuItem);
        }
        MenuItem result = menuItemService.save(menuItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("menuItem", menuItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /menu-items : get all the menuItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of menuItems in body
     */
    @GetMapping("/menu-items")
    @Timed
    public List<MenuItem> getAllMenuItems() {
        log.debug("REST request to get all MenuItems");
        return menuItemService.findAll();
    }

    /**
     * GET  /menu-items/:id : get the "id" menuItem.
     *
     * @param id the id of the menuItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the menuItem, or with status 404 (Not Found)
     */
    @GetMapping("/menu-items/{id}")
    @Timed
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        MenuItem menuItem = menuItemService.findOne(id);
        return Optional.ofNullable(menuItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /menu-items/:id : delete the "id" menuItem.
     *
     * @param id the id of the menuItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/menu-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("menuItem", id.toString())).build();
    }

}
