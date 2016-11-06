package com.intelli5.back.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.intelli5.back.domain.Ticket;
import com.intelli5.back.service.TicketService;
import com.intelli5.back.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ticket.
 */
@RestController
@RequestMapping("/api")
public class TicketResource {

    private final Logger log = LoggerFactory.getLogger(TicketResource.class);
        
    @Inject
    private TicketService ticketService;

    /**
     * POST  /tickets : Create a new ticket.
     *
     * @param ticket the ticket to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ticket, or with status 400 (Bad Request) if the ticket has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tickets")
    @Timed
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) throws URISyntaxException {
        log.debug("REST request to save Ticket : {}", ticket);
        if (ticket.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ticket", "idexists", "A new ticket cannot already have an ID")).body(null);
        }
        Ticket result = ticketService.save(ticket);
        return ResponseEntity.created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ticket", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tickets : Updates an existing ticket.
     *
     * @param ticket the ticket to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ticket,
     * or with status 400 (Bad Request) if the ticket is not valid,
     * or with status 500 (Internal Server Error) if the ticket couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tickets")
    @Timed
    public ResponseEntity<Ticket> updateTicket(@RequestBody Ticket ticket) throws URISyntaxException {
        log.debug("REST request to update Ticket : {}", ticket);
        if (ticket.getId() == null) {
            return createTicket(ticket);
        }
        Ticket result = ticketService.save(ticket);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ticket", ticket.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tickets : get all the tickets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tickets in body
     */
    @GetMapping("/tickets")
    @Timed
    public List<Ticket> getAllTickets() {
        log.debug("REST request to get all Tickets");
        return ticketService.findAll();
    }

    /**
     * GET  /tickets/:id : get the "id" ticket.
     *
     * @param id the id of the ticket to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ticket, or with status 404 (Not Found)
     */
    @GetMapping("/tickets/{id}")
    @Timed
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        log.debug("REST request to get Ticket : {}", id);
        Ticket ticket = ticketService.findOne(id);
        return Optional.ofNullable(ticket)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tickets/:id : delete the "id" ticket.
     *
     * @param id the id of the ticket to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tickets/{id}")
    @Timed
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.debug("REST request to delete Ticket : {}", id);
        ticketService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ticket", id.toString())).build();
    }

}
