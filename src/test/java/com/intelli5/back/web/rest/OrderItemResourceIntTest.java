package com.intelli5.back.web.rest;

import com.intelli5.back.FoodininjaApp;

import com.intelli5.back.domain.OrderItem;
import com.intelli5.back.repository.OrderItemRepository;
import com.intelli5.back.service.OrderItemService;

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
import java.math.BigDecimal;
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

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String DEFAULT_IMAGE_URL = "AAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBB";

    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
    private OrderItemService orderItemService;

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
                .quantity(DEFAULT_QUANTITY)
                .name(DEFAULT_NAME)
                .price(DEFAULT_PRICE)
                .imageUrl(DEFAULT_IMAGE_URL);
        return orderItem;
    }

    @Before
    public void initTest() {
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
        assertThat(testOrderItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrderItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderItem.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
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
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())));
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
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()));
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
                .quantity(UPDATED_QUANTITY)
                .name(UPDATED_NAME)
                .price(UPDATED_PRICE)
                .imageUrl(UPDATED_IMAGE_URL);

        restOrderItemMockMvc.perform(put("/api/order-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderItem)))
                .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItems.get(orderItems.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrderItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderItem.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
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

        // Validate the database is empty
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
