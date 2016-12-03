package com.intelli5.back.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.intelli5.back.domain.FoodJoint;
import com.intelli5.back.domain.FoodOrder;
import com.intelli5.back.domain.OrderItem;
import com.intelli5.back.domain.Ticket;
import com.intelli5.back.domain.enumeration.TicketStatus;
import com.intelli5.back.service.FoodJointService;
import com.intelli5.back.service.OrderItemService;
import com.intelli5.back.service.TicketService;
import com.intelli5.back.service.dto.TicketDTO;
import com.intelli5.back.service.dto.TicketGo;
import com.intelli5.back.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing Ticket.
 */
@RestController
@RequestMapping("/api")
public class TicketResource {

    private final Logger log = LoggerFactory.getLogger(TicketResource.class);

    @Inject
    private TicketService ticketService;

    @Inject
    private OrderItemService orderItemService;

    @Inject
    private FoodJointService foodJointService;

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
     * GET  /tickets/foodjoint/:id : get all the tickets by food joint.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tickets in body
     */
    @GetMapping("/tickets/foodjoint/{id}")
    @Timed
    public List<Ticket> findByFoodJoint_Id(@PathVariable Long id) {
        log.debug("REST request to get findByFoodJoint_Id");
        return ticketService.findByFoodJoint_Id(id);
    }

    /**
     * GET  /tickets/user/:id : get all the tickets by food joint.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tickets in body
     */
    @GetMapping("/tickets/user/{id}")
    @Timed
    public List<Ticket> findByUser_Id(@PathVariable Long id) {
        log.debug("REST request to get findByUser_Id");
        List<Ticket> tickets = ticketService.findByUser_Id(id);
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.FINISH) {
                tickets.remove(ticket);
            }
        }
        return tickets;
    }

    /**
     * GET  /tickets/username/:name : get all the tickets by food joint.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tickets in body
     */
    @GetMapping("/tickets/username/{name}")
    @Timed
    public List<TicketGo> findByUser_Name(@PathVariable String name) {
        log.debug("REST request to get findByUser_Id");
        List<TicketGo> tickets = ticketService.findByUser_Login(name);
//        for (TicketGo ticket : tickets) {
//            if (ticket.getStatus() == TicketStatus.FINISH) {
//                tickets.remove(ticket);
//            }
//        }
        return tickets;
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
     * GET  /tickets/new : get new ticket.
     *
     * @param foodJointId the id of the ticket to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ticket, or with status 404 (Not Found)
     */
    @GetMapping("/tickets/new")
    @Timed
    public ResponseEntity<Ticket> getNewTicket(@RequestParam Long foodJointId, @RequestParam String userName) throws URISyntaxException {
        log.debug("REST request to get new Ticket : {}", foodJointId);
        FoodJoint foodJoint = foodJointService.findOne(foodJointId);
        Ticket ticket = ticketService.getNewTicket(foodJoint, userName);
        ticket.setStatus(TicketStatus.NO_ORDER_WAIT);
        ticket = ticketService.save(ticket);
        return ResponseEntity.created(new URI("/api/tickets/" + ticket.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ticket", ticket.getId().toString()))
            .body(ticket);
    }

    /**
     * GET  /tickets/next : get the next ticket.
     *
     * @param ticketDTO foodJointId, previousTicketId, previousTicketStatus(FINISH or SKIP)
     * @return the ResponseEntity with status 200 (OK) and with body the ticket, or with status 404 (Not Found)
     */
    @PostMapping("/tickets/next")
    @Timed
    public ResponseEntity<String> setPreviousTicketStatus(@RequestBody TicketDTO ticketDTO) {
        log.debug("REST request to getNextTicketSetPreviousTicketStatus Ticket : {}", ticketDTO);
        Long preTickId = ticketDTO.getPreviousTicketId();
        if (preTickId != null && preTickId > 0) {
            Ticket previousTicket = ticketService.findOne(preTickId);
            previousTicket.setStatus(ticketDTO.getPreviousTicketStatus());
            ticketService.save(previousTicket);
        }
        return ResponseEntity.ok("OK");
    }

    /**
     * POST  /tickets/next_has_order : get the next ticket.
     *
     * @param ticketDTO foodJointId, previousTicketId, previousTicketStatus(READY or SKIP)
     * @return the ResponseEntity with status 200 (OK) and with body the ticket, or with status 404 (Not Found)
     */
    @PostMapping("/tickets/next_has_order")
    @Timed
    public ResponseEntity<Ticket> getNextHasOrderTicketSetPreviousTicketStatus(@RequestBody TicketDTO ticketDTO) {
        log.debug("REST request to getNextOrderTicketSetPreviousTicketStatus Ticket : {}", ticketDTO);
        Long preTickId = ticketDTO.getPreviousTicketId();
        if(preTickId != null && preTickId > 0){
            Ticket previousTicket = ticketService.findOne(preTickId);
            previousTicket.setStatus(ticketDTO.getPreviousTicketStatus());
            ticketService.save(previousTicket);
        }
        List<TicketStatus> candidateStatues = new ArrayList<>();
        candidateStatues.add(TicketStatus.WAIT);
        Ticket ticket = ticketService.getNextTicket(ticketDTO.getFoodJointId(), candidateStatues);
        return Optional.ofNullable(ticket)
            .map(result ->
            {
                result.setStatus(TicketStatus.PROCESS);
                ticketService.save(result);
                FoodOrder foodOrder = result.getFoodOrder();
                Set<OrderItem> orderItems = new HashSet<OrderItem>(orderItemService.findByFoodOrder_Id(foodOrder.getId()));
                foodOrder.setOrderItems(orderItems);
                return new ResponseEntity<>(
                result,
                HttpStatus.OK);
            })
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

    /**
     * SEARCH  /_search/tickets?query=:query : search for the ticket corresponding
     * to the query.
     *
     * @param query the query of the ticket search
     * @return the result of the search
     */
    @GetMapping("/_search/tickets")
    @Timed
    public List<Ticket> searchTickets(@RequestParam String query) {
        log.debug("REST request to search Tickets for query {}", query);
        return ticketService.search(query);
    }


}
