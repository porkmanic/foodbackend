package com.intelli5.back.web.rest;

import com.intelli5.back.FoodininjaApp;

import com.intelli5.back.domain.Ticket;
import com.intelli5.back.repository.TicketRepository;
import com.intelli5.back.service.TicketService;
import com.intelli5.back.repository.search.TicketSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intelli5.back.domain.enumeration.TicketStatus;
/**
 * Test class for the TicketResource REST controller.
 *
 * @see TicketResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodininjaApp.class)
public class TicketResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final byte[] DEFAULT_QR_CODE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_QR_CODE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_QR_CODE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_QR_CODE_CONTENT_TYPE = "image/png";

    private static final TicketStatus DEFAULT_STATUS = TicketStatus.WAIT;
    private static final TicketStatus UPDATED_STATUS = TicketStatus.PROCESS;

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATE_TIME);

    private static final ZonedDateTime DEFAULT_ESTIMATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_ESTIMATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ESTIMATE_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_ESTIMATE_TIME);

    @Inject
    private TicketRepository ticketRepository;

    @Inject
    private TicketService ticketService;

    @Inject
    private TicketSearchRepository ticketSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TicketResource ticketResource = new TicketResource();
        ReflectionTestUtils.setField(ticketResource, "ticketService", ticketService);
        this.restTicketMockMvc = MockMvcBuilders.standaloneSetup(ticketResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        Ticket ticket = new Ticket()
                .number(DEFAULT_NUMBER)
                .qrCode(DEFAULT_QR_CODE)
                .qrCodeContentType(DEFAULT_QR_CODE_CONTENT_TYPE)
                .status(DEFAULT_STATUS)
                .createTime(DEFAULT_CREATE_TIME)
                .estimateTime(DEFAULT_ESTIMATE_TIME);
        return ticket;
    }

    @Before
    public void initTest() {
        ticketSearchRepository.deleteAll();
        ticket = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // Create the Ticket

        restTicketMockMvc.perform(post("/api/tickets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ticket)))
                .andExpect(status().isCreated());

        // Validate the Ticket in the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = tickets.get(tickets.size() - 1);
        assertThat(testTicket.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTicket.getQrCode()).isEqualTo(DEFAULT_QR_CODE);
        assertThat(testTicket.getQrCodeContentType()).isEqualTo(DEFAULT_QR_CODE_CONTENT_TYPE);
        assertThat(testTicket.getStatus()).isEqualTo(TicketStatus.NO_ORDER_WAIT);
        assertThat(testTicket.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testTicket.getEstimateTime()).isEqualTo(DEFAULT_ESTIMATE_TIME);

        // Validate the Ticket in ElasticSearch
        Ticket ticketEs = ticketSearchRepository.findOne(testTicket.getId());
        assertThat(ticketEs).isEqualToComparingFieldByField(testTicket);
    }

    @Test
    @Transactional
    public void getAllTickets() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the tickets
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].qrCodeContentType").value(hasItem(DEFAULT_QR_CODE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].qrCode").value(hasItem(Base64Utils.encodeToString(DEFAULT_QR_CODE))))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].estimateTime").value(hasItem(DEFAULT_ESTIMATE_TIME_STR)));
    }

    @Test
    @Transactional
    public void getTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.qrCodeContentType").value(DEFAULT_QR_CODE_CONTENT_TYPE))
            .andExpect(jsonPath("$.qrCode").value(Base64Utils.encodeToString(DEFAULT_QR_CODE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME_STR))
            .andExpect(jsonPath("$.estimateTime").value(DEFAULT_ESTIMATE_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTicket() throws Exception {
        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicket() throws Exception {
        // Initialize the database
        ticketService.save(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findOne(ticket.getId());
        updatedTicket
                .number(UPDATED_NUMBER)
                .qrCode(UPDATED_QR_CODE)
                .qrCodeContentType(UPDATED_QR_CODE_CONTENT_TYPE)
                .status(UPDATED_STATUS)
                .createTime(UPDATED_CREATE_TIME)
                .estimateTime(UPDATED_ESTIMATE_TIME);

        restTicketMockMvc.perform(put("/api/tickets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTicket)))
                .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = tickets.get(tickets.size() - 1);
        assertThat(testTicket.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTicket.getQrCode()).isEqualTo(UPDATED_QR_CODE);
        assertThat(testTicket.getQrCodeContentType()).isEqualTo(UPDATED_QR_CODE_CONTENT_TYPE);
        assertThat(testTicket.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicket.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testTicket.getEstimateTime()).isEqualTo(UPDATED_ESTIMATE_TIME);

        // Validate the Ticket in ElasticSearch
        Ticket ticketEs = ticketSearchRepository.findOne(testTicket.getId());
        assertThat(ticketEs).isEqualToComparingFieldByField(testTicket);
    }

    @Test
    @Transactional
    public void deleteTicket() throws Exception {
        // Initialize the database
        ticketService.save(ticket);

        int databaseSizeBeforeDelete = ticketRepository.findAll().size();

        // Get the ticket
        restTicketMockMvc.perform(delete("/api/tickets/{id}", ticket.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ticketExistsInEs = ticketSearchRepository.exists(ticket.getId());
        assertThat(ticketExistsInEs).isFalse();

        // Validate the database is empty
        List<Ticket> tickets = ticketRepository.findAll();
        assertThat(tickets).hasSize(databaseSizeBeforeDelete - 1);
    }

//    @Test
//    @Transactional
//    public void searchTicket() throws Exception {
//        // Initialize the database
//        ticketService.save(ticket);
//
//        // Search the ticket
//        restTicketMockMvc.perform(get("/api/_search/tickets?query=id:" + ticket.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
//            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
//            .andExpect(jsonPath("$.[*].qrCodeContentType").value(hasItem(DEFAULT_QR_CODE_CONTENT_TYPE)))
//            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(Base64Utils.encodeToString(DEFAULT_QR_CODE))))
//            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
//            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME_STR)));
////            .andExpect(jsonPath("$.[*].estimateTime").value(hasItem(DEFAULT_ESTIMATE_TIME_STR)));
//    }
}
