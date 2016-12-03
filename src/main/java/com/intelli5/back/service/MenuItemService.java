package com.intelli5.back.service;

import com.intelli5.back.domain.MenuItem;
import com.intelli5.back.repository.MenuItemRepository;
import com.intelli5.back.repository.search.MenuItemSearchRepository;
import com.intelli5.back.service.dto.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing MenuItem.
 */
@Service
@Transactional
public class MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemService.class);

    @Inject
    private MenuItemRepository menuItemRepository;

    @Inject
    private MenuItemSearchRepository menuItemSearchRepository;

    /**
     * Save a menuItem.
     *
     * @param menuItem the entity to save
     * @return the persisted entity
     */
    public MenuItem save(MenuItem menuItem) {
        log.debug("Request to save MenuItem : {}", menuItem);
        MenuItem result = menuItemRepository.save(menuItem);
        menuItemSearchRepository.save(result);
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
     *  Get Price.
     *
     *  @param itemDTOs the items of the entity
     *  @return the price
     */
    @Transactional(readOnly = true)
    public BigDecimal getPrice(List<ItemDTO> itemDTOs) {
        log.debug("Request to get Price ");
        BigDecimal price = BigDecimal.ZERO;
        for (ItemDTO itemDTO: itemDTOs) {
            MenuItem menuItem = menuItemRepository.findOne(itemDTO.getId());
            price = calculatePrice(price, menuItem.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
        }
        return price;
    }

    private BigDecimal calculatePrice(BigDecimal price, BigDecimal newPrice) {
        return price.add(newPrice);
    }

    /**
     *  Get one menuItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public List<MenuItem> findByFoodJoint_Id(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        List<MenuItem> menuItems = menuItemRepository.findByFoodJoint_Id(id);
        return menuItems;
    }

    /**
     *  Delete the  menuItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.delete(id);
        menuItemSearchRepository.delete(id);
    }

    /**
     * Search for the menuItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MenuItem> search(String query) {
        log.debug("Request to search MenuItems for query {}", query);
        return StreamSupport
            .stream(menuItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
