package com.intelli5.back.service;

import com.intelli5.back.domain.Ticket;
import com.intelli5.back.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Ticket.
 */
@Service
@Transactional
public class TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketService.class);
    
    @Inject
    private TicketRepository ticketRepository;

    /**
     * Save a ticket.
     *
     * @param ticket the entity to save
     * @return the persisted entity
     */
    public Ticket save(Ticket ticket) {
        log.debug("Request to save Ticket : {}", ticket);
        Ticket result = ticketRepository.save(ticket);
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
    }
}
