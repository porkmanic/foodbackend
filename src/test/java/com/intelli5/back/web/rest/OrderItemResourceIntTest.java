package com.intelli5.back.web.rest;

import com.intelli5.back.FoodininjaApp;

import com.intelli5.back.domain.OrderItem;
import com.intelli5.back.repository.OrderItemRepository;
import com.intelli5.back.service.OrderItemService;
import com.intelli5.back.repository.search.OrderItemSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderItemResource REST controller.
 *
 * @see OrderItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodininjaApp.class)
public class OrderItemResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
    private OrderItemService orderItemService;

    @Inject
    private OrderItemSearchRepository orderItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderItemResource orderItemResource = new OrderItemResource();
        ReflectionTestUtils.setField(orderItemResource, "orderItemService", orderItemService);
        this.restOrderItemMockMvc = MockMvcBuilders.standaloneSetup(orderItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
                .quantity(DEFAULT_QUANTITY);
        return orderItem;
    }

    @Before
    public void initTest() {
        orderItemSearchRepository.deleteAll();
        orderItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // Create the OrderItem

        restOrderItemMockMvc.perform(post("/api/order-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItems.get(orderItems.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the OrderItem in ElasticSearch
        OrderItem orderItemEs = orderItemSearchRepository.findOne(testOrderItem.getId());
        assertThat(orderItemEs).isEqualToComparingFieldByField(testOrderItem);
    }

    @Test
    @Transactional
    public void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItems
        restOrderItemMockMvc.perform(get("/api/order-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/order-items/{id}", orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/order-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderItem() throws Exception {
        // Initialize the database
        orderItemService.save(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findOne(orderItem.getId());
        updatedOrderItem
                .quantity(UPDATED_QUANTITY);

        restOrderItemMockMvc.perform(put("/api/order-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderItem)))
                .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItems.get(orderItems.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the OrderItem in ElasticSearch
        OrderItem orderItemEs = orderItemSearchRepository.findOne(testOrderItem.getId());
        assertThat(orderItemEs).isEqualToComparingFieldByField(testOrderItem);
    }

    @Test
    @Transactional
    public void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemService.save(orderItem);

        int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Get the orderItem
        restOrderItemMockMvc.perform(delete("/api/order-items/{id}", orderItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderItemExistsInEs = orderItemSearchRepository.exists(orderItem.getId());
        assertThat(orderItemExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderItem() throws Exception {
        // Initialize the database
        orderItemService.save(orderItem);

        // Search the orderItem
        restOrderItemMockMvc.perform(get("/api/_search/order-items?query=id:" + orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
}
