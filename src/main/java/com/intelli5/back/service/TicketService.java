package com.intelli5.back.service;

import com.intelli5.back.domain.Ticket;
import com.intelli5.back.repository.TicketRepository;
import com.intelli5.back.repository.search.TicketSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ticket.
 */
@Service
@Transactional
public class TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketService.class);
    
    @Inject
    private TicketRepository ticketRepository;

    @Inject
    private TicketSearchRepository ticketSearchRepository;

    /**
     * Save a ticket.
     *
     * @param ticket the entity to save
     * @return the persisted entity
     */
    public Ticket save(Ticket ticket) {
        log.debug("Request to save Ticket : {}", ticket);
        Ticket result = ticketRepository.save(ticket);
        ticketSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the tickets.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Ticket> findAll() {
        log.debug("Request to get all Tickets");
        List<Ticket> result = ticketRepository.findAll();

        return result;
    }

    /**
     *  Get one ticket by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Ticket findOne(Long id) {
        log.debug("Request to get Ticket : {}", id);
        Ticket ticket = ticketRepository.findOne(id);
        return ticket;
    }

    /**
     *  Delete the  ticket by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        ticketRepository.delete(id);
        ticketSearchRepository.delete(id);
    }

    /**
     * Search for the ticket corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Ticket> search(String query) {
        log.debug("Request to search Tickets for query {}", query);
        return StreamSupport
            .stream(ticketSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
