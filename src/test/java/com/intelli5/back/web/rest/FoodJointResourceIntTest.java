package com.intelli5.back.web.rest;

import com.intelli5.back.FoodininjaApp;

import com.intelli5.back.domain.FoodJoint;
import com.intelli5.back.repository.FoodJointRepository;
import com.intelli5.back.service.FoodJointService;
import com.intelli5.back.repository.search.FoodJointSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FoodJointResource REST controller.
 *
 * @see FoodJointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodininjaApp.class)
public class FoodJointResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_WORKING_HOURS = "AAAAA";
    private static final String UPDATED_WORKING_HOURS = "BBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Float DEFAULT_ESTIMAT_WAIT_PER_PERSON = 1F;
    private static final Float UPDATED_ESTIMAT_WAIT_PER_PERSON = 2F;

    @Inject
    private FoodJointRepository foodJointRepository;

    @Inject
    private FoodJointService foodJointService;

    @Inject
    private FoodJointSearchRepository foodJointSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFoodJointMockMvc;

    private FoodJoint foodJoint;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FoodJointResource foodJointResource = new FoodJointResource();
        ReflectionTestUtils.setField(foodJointResource, "foodJointService", foodJointService);
        this.restFoodJointMockMvc = MockMvcBuilders.standaloneSetup(foodJointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodJoint createEntity(EntityManager em) {
        FoodJoint foodJoint = new FoodJoint()
                .name(DEFAULT_NAME)
                .workingHours(DEFAULT_WORKING_HOURS)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .estimatWaitPerPerson(DEFAULT_ESTIMAT_WAIT_PER_PERSON);
        return foodJoint;
    }

    @Before
    public void initTest() {
        foodJointSearchRepository.deleteAll();
        foodJoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createFoodJoint() throws Exception {
        int databaseSizeBeforeCreate = foodJointRepository.findAll().size();

        // Create the FoodJoint

        restFoodJointMockMvc.perform(post("/api/food-joints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(foodJoint)))
                .andExpect(status().isCreated());

        // Validate the FoodJoint in the database
        List<FoodJoint> foodJoints = foodJointRepository.findAll();
        assertThat(foodJoints).hasSize(databaseSizeBeforeCreate + 1);
        FoodJoint testFoodJoint = foodJoints.get(foodJoints.size() - 1);
        assertThat(testFoodJoint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFoodJoint.getWorkingHours()).isEqualTo(DEFAULT_WORKING_HOURS);
        assertThat(testFoodJoint.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFoodJoint.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testFoodJoint.getEstimatWaitPerPerson()).isEqualTo(DEFAULT_ESTIMAT_WAIT_PER_PERSON);

        // Validate the FoodJoint in ElasticSearch
        FoodJoint foodJointEs = foodJointSearchRepository.findOne(testFoodJoint.getId());
        assertThat(foodJointEs).isEqualToComparingFieldByField(testFoodJoint);
    }

    @Test
    @Transactional
    public void getAllFoodJoints() throws Exception {
        // Initialize the database
        foodJointRepository.saveAndFlush(foodJoint);

        // Get all the foodJoints
        restFoodJointMockMvc.perform(get("/api/food-joints?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(foodJoint.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].workingHours").value(hasItem(DEFAULT_WORKING_HOURS.toString())))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].estimatWaitPerPerson").value(hasItem(DEFAULT_ESTIMAT_WAIT_PER_PERSON.doubleValue())));
    }

    @Test
    @Transactional
    public void getFoodJoint() throws Exception {
        // Initialize the database
        foodJointRepository.saveAndFlush(foodJoint);

        // Get the foodJoint
        restFoodJointMockMvc.perform(get("/api/food-joints/{id}", foodJoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(foodJoint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.workingHours").value(DEFAULT_WORKING_HOURS.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.estimatWaitPerPerson").value(DEFAULT_ESTIMAT_WAIT_PER_PERSON.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFoodJoint() throws Exception {
        // Get the foodJoint
        restFoodJointMockMvc.perform(get("/api/food-joints/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFoodJoint() throws Exception {
        // Initialize the database
        foodJointService.save(foodJoint);

        int databaseSizeBeforeUpdate = foodJointRepository.findAll().size();

        // Update the foodJoint
        FoodJoint updatedFoodJoint = foodJointRepository.findOne(foodJoint.getId());
        updatedFoodJoint
                .name(UPDATED_NAME)
                .workingHours(UPDATED_WORKING_HOURS)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .estimatWaitPerPerson(UPDATED_ESTIMAT_WAIT_PER_PERSON);

        restFoodJointMockMvc.perform(put("/api/food-joints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFoodJoint)))
                .andExpect(status().isOk());

        // Validate the FoodJoint in the database
        List<FoodJoint> foodJoints = foodJointRepository.findAll();
        assertThat(foodJoints).hasSize(databaseSizeBeforeUpdate);
        FoodJoint testFoodJoint = foodJoints.get(foodJoints.size() - 1);
        assertThat(testFoodJoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFoodJoint.getWorkingHours()).isEqualTo(UPDATED_WORKING_HOURS);
        assertThat(testFoodJoint.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFoodJoint.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testFoodJoint.getEstimatWaitPerPerson()).isEqualTo(UPDATED_ESTIMAT_WAIT_PER_PERSON);

        // Validate the FoodJoint in ElasticSearch
        FoodJoint foodJointEs = foodJointSearchRepository.findOne(testFoodJoint.getId());
        assertThat(foodJointEs).isEqualToComparingFieldByField(testFoodJoint);
    }

    @Test
    @Transactional
    public void deleteFoodJoint() throws Exception {
        // Initialize the database
        foodJointService.save(foodJoint);

        int databaseSizeBeforeDelete = foodJointRepository.findAll().size();

        // Get the foodJoint
        restFoodJointMockMvc.perform(delete("/api/food-joints/{id}", foodJoint.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean foodJointExistsInEs = foodJointSearchRepository.exists(foodJoint.getId());
        assertThat(foodJointExistsInEs).isFalse();

        // Validate the database is empty
        List<FoodJoint> foodJoints = foodJointRepository.findAll();
        assertThat(foodJoints).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFoodJoint() throws Exception {
        // Initialize the database
        foodJointService.save(foodJoint);

        // Search the foodJoint
        restFoodJointMockMvc.perform(get("/api/_search/food-joints?query=id:" + foodJoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodJoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].workingHours").value(hasItem(DEFAULT_WORKING_HOURS.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].estimatWaitPerPerson").value(hasItem(DEFAULT_ESTIMAT_WAIT_PER_PERSON.doubleValue())));
    }
}
