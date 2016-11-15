package com.intelli5.back.web.rest;

import com.intelli5.back.FoodininjaApp;

import com.intelli5.back.domain.MenuItem;
import com.intelli5.back.repository.MenuItemRepository;
import com.intelli5.back.service.MenuItemService;
import com.intelli5.back.repository.search.MenuItemSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MenuItemResource REST controller.
 *
 * @see MenuItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodininjaApp.class)
public class MenuItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private MenuItemRepository menuItemRepository;

    @Inject
    private MenuItemService menuItemService;

    @Inject
    private MenuItemSearchRepository menuItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MenuItemResource menuItemResource = new MenuItemResource();
        ReflectionTestUtils.setField(menuItemResource, "menuItemService", menuItemService);
        this.restMenuItemMockMvc = MockMvcBuilders.standaloneSetup(menuItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
                .name(DEFAULT_NAME)
                .price(DEFAULT_PRICE)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .description(DEFAULT_DESCRIPTION);
        return menuItem;
    }

    @Before
    public void initTest() {
        menuItemSearchRepository.deleteAll();
        menuItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem

        restMenuItemMockMvc.perform(post("/api/menu-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(menuItem)))
                .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertThat(menuItems).hasSize(databaseSizeBeforeCreate + 1);
        MenuItem testMenuItem = menuItems.get(menuItems.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMenuItem.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMenuItem.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the MenuItem in ElasticSearch
        MenuItem menuItemEs = menuItemSearchRepository.findOne(testMenuItem.getId());
        assertThat(menuItemEs).isEqualToComparingFieldByField(testMenuItem);
    }

    @Test
    @Transactional
    public void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItems
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findOne(menuItem.getId());
        updatedMenuItem
                .name(UPDATED_NAME)
                .price(UPDATED_PRICE)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .description(UPDATED_DESCRIPTION);

        restMenuItemMockMvc.perform(put("/api/menu-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMenuItem)))
                .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertThat(menuItems).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItems.get(menuItems.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMenuItem.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMenuItem.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the MenuItem in ElasticSearch
        MenuItem menuItemEs = menuItemSearchRepository.findOne(testMenuItem.getId());
        assertThat(menuItemEs).isEqualToComparingFieldByField(testMenuItem);
    }

    @Test
    @Transactional
    public void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);

        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Get the menuItem
        restMenuItemMockMvc.perform(delete("/api/menu-items/{id}", menuItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean menuItemExistsInEs = menuItemSearchRepository.exists(menuItem.getId());
        assertThat(menuItemExistsInEs).isFalse();

        // Validate the database is empty
        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertThat(menuItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);

        // Search the menuItem
        restMenuItemMockMvc.perform(get("/api/_search/menu-items?query=id:" + menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
