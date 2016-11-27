package com.intelli5.back.service;

import com.intelli5.back.domain.OrderItem;
import com.intelli5.back.repository.OrderItemRepository;
import com.intelli5.back.repository.search.OrderItemSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing OrderItem.
 */
@Service
@Transactional
public class OrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
    private OrderItemSearchRepository orderItemSearchRepository;

    /**
     * Save a orderItem.
     *
     * @param orderItem the entity to save
     * @return the persisted entity
     */
    public OrderItem save(OrderItem orderItem) {
        log.debug("Request to save OrderItem : {}", orderItem);
        OrderItem result = orderItemRepository.save(orderItem);
        orderItemSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the orderItems.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        log.debug("Request to get all OrderItems");
        List<OrderItem> result = orderItemRepository.findAll();
        return result;
    }

    /**
     * Get all the orderItems.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrderItem> findByFoodOrder_Id(Long foodOrderId) {
        log.debug("Request to get all OrderItems");
        List<OrderItem> result = orderItemRepository.findByFoodOrder_Id(foodOrderId);
        return result;
    }

    /**
     *  Get one orderItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OrderItem findOne(Long id) {
        log.debug("Request to get OrderItem : {}", id);
        OrderItem orderItem = orderItemRepository.findOne(id);
        return orderItem;
    }

    /**
     *  Delete the  orderItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderItem : {}", id);
        orderItemRepository.delete(id);
        orderItemSearchRepository.delete(id);
    }

    /**
     * Search for the orderItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrderItem> search(String query) {
        log.debug("Request to search OrderItems for query {}", query);
        return StreamSupport
            .stream(orderItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
