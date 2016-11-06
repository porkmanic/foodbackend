package com.intelli5.back.web.rest;

import com.intelli5.back.FoodininjaApp;

import com.intelli5.back.domain.FoodOrder;
import com.intelli5.back.repository.FoodOrderRepository;
import com.intelli5.back.service.FoodOrderService;

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
 * Test class for the FoodOrderResource REST controller.
 *
 * @see FoodOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodininjaApp.class)
public class FoodOrderResourceIntTest {

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    @Inject
    private FoodOrderRepository foodOrderRepository;

    @Inject
    private FoodOrderService foodOrderService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFoodOrderMockMvc;

    private FoodOrder foodOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FoodOrderResource foodOrderResource = new FoodOrderResource();
        ReflectionTestUtils.setField(foodOrderResource, "foodOrderService", foodOrderService);
        this.restFoodOrderMockMvc = MockMvcBuilders.standaloneSetup(foodOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodOrder createEntity(EntityManager em) {
        FoodOrder foodOrder = new FoodOrder()
                .totalPrice(DEFAULT_TOTAL_PRICE);
        return foodOrder;
    }

    @Before
    public void initTest() {
        foodOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createFoodOrder() throws Exception {
        int databaseSizeBeforeCreate = foodOrderRepository.findAll().size();

        // Create the FoodOrder

        restFoodOrderMockMvc.perform(post("/api/food-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foodOrder)))
                .andExpect(status().isCreated());

        // Validate the FoodOrder in the database
        List<FoodOrder> foodOrders = foodOrderRepository.findAll();
        assertThat(foodOrders).hasSize(databaseSizeBeforeCreate + 1);
        FoodOrder testFoodOrder = foodOrders.get(foodOrders.size() - 1);
        assertThat(testFoodOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllFoodOrders() throws Exception {
        // Initialize the database
        foodOrderRepository.saveAndFlush(foodOrder);

        // Get all the foodOrders
        restFoodOrderMockMvc.perform(get("/api/food-orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(foodOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getFoodOrder() throws Exception {
        // Initialize the database
        foodOrderRepository.saveAndFlush(foodOrder);

        // Get the foodOrder
        restFoodOrderMockMvc.perform(get("/api/food-orders/{id}", foodOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(foodOrder.getId().intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFoodOrder() throws Exception {
        // Get the foodOrder
        restFoodOrderMockMvc.perform(get("/api/food-orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFoodOrder() throws Exception {
        // Initialize the database
        foodOrderService.save(foodOrder);

        int databaseSizeBeforeUpdate = foodOrderRepository.findAll().size();

        // Update the foodOrder
        FoodOrder updatedFoodOrder = foodOrderRepository.findOne(foodOrder.getId());
        updatedFoodOrder
                .totalPrice(UPDATED_TOTAL_PRICE);

        restFoodOrderMockMvc.perform(put("/api/food-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFoodOrder)))
                .andExpect(status().isOk());

        // Validate the FoodOrder in the database
        List<FoodOrder> foodOrders = foodOrderRepository.findAll();
        assertThat(foodOrders).hasSize(databaseSizeBeforeUpdate);
        FoodOrder testFoodOrder = foodOrders.get(foodOrders.size() - 1);
        assertThat(testFoodOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void deleteFoodOrder() throws Exception {
        // Initialize the database
        foodOrderService.save(foodOrder);

        int databaseSizeBeforeDelete = foodOrderRepository.findAll().size();

        // Get the foodOrder
        restFoodOrderMockMvc.perform(delete("/api/food-orders/{id}", foodOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FoodOrder> foodOrders = foodOrderRepository.findAll();
        assertThat(foodOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
