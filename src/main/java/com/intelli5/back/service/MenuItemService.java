package com.intelli5.back.service;

import com.intelli5.back.domain.MenuItem;
import com.intelli5.back.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing MenuItem.
 */
@Service
@Transactional
public class MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemService.class);
    
    @Inject
    private MenuItemRepository menuItemRepository;

    /**
     * Save a menuItem.
     *
     * @param menuItem the entity to save
     * @return the persisted entity
     */
    public MenuItem save(MenuItem menuItem) {
        log.debug("Request to save MenuItem : {}", menuItem);
        MenuItem result = menuItemRepository.save(menuItem);
        return result;
    }

    /**
     *  Get all the menuItems.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<MenuItem> findAll() {
        log.debug("Request to get all MenuItems");
        List<MenuItem> result = menuItemRepository.findAll();

        return result;
    }

    /**
     *  Get one menuItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MenuItem findOne(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        MenuItem menuItem = menuItemRepository.findOne(id);
        return menuItem;
    }

    /**
     *  Delete the  menuItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.delete(id);
    }
}
