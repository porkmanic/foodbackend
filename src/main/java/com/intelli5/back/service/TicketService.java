package com.intelli5.back.service;

import com.intelli5.back.domain.FoodJoint;
import com.intelli5.back.domain.Ticket;
import com.intelli5.back.domain.User;
import com.intelli5.back.domain.enumeration.TicketStatus;
import com.intelli5.back.repository.TicketRepository;
import com.intelli5.back.repository.UserRepository;
import com.intelli5.back.repository.search.TicketSearchRepository;
import com.intelli5.back.service.dto.TicketGo;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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

    @Inject
    private UserRepository userRepository;

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
     *  Get one ticket by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public List<Ticket> findByFoodJoint_Id(Long id) {
        log.debug("Request to findByFoodJoint_Id : {}", id);
        List<TicketStatus> candidateStatues = new ArrayList<>();
        candidateStatues.add(TicketStatus.NO_ORDER_WAIT);
        candidateStatues.add(TicketStatus.READY);
        List<Ticket> tickets = ticketRepository.findByFoodJoint_IdAndStatusIn(id, candidateStatues);
        return tickets;
    }

    /**
     * Get one ticket by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<Ticket> findByUser_Id(Long id) {
        log.debug("Request to findByUser_Id : {}", id);
        List<Ticket> tickets = ticketRepository.findByUser_Id(id);
        return tickets;
    }

    /**
     * Get one ticket by id.
     *
     * @param name the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<TicketGo> findByUser_Login(String name) {
        log.debug("Request to findByUser_Login : {}", name);
        List<Ticket> tickets = ticketRepository.findByUser_Login(name);
        List<TicketGo> results = new ArrayList<>();
        if (tickets != null) {
            for (Ticket ticket : tickets) {
                results.add(getGoTicket(ticket));
            }
        }
        return results;
    }

    public TicketGo getGoTicket(Ticket ticket) {
        TicketGo ticketGo = new TicketGo(ticket);
        if (ticket.getStatus() == TicketStatus.FINISH || ticket.getStatus() == TicketStatus.CANCEL || ticket.getStatus() == TicketStatus.SKIP) {
            ticketGo.setEstimateTime(0);
        } else {
            Float perPerson = ticket.getFoodJoint().getEstimatWaitPerPerson();
            List<TicketStatus> ticketStatuses = new ArrayList<>();
            ticketStatuses.add(TicketStatus.NO_ORDER_WAIT);
            ticketStatuses.add(TicketStatus.WAIT);
            ticketStatuses.add(TicketStatus.READY);
            ticketStatuses.add(TicketStatus.PROCESS);
            Long personInline = ticketRepository.countByFoodJoint_IdAndStatusInAndIdLessThan(ticket.getFoodJoint().getId(), ticketStatuses, ticket.getId());
            ticketGo.setEstimateTime((int) (personInline * perPerson));
        }
        return ticketGo;
    }

    /**
     *  Get one ticket by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public int getQueueNumber(Long id) {
        log.debug("Request to get getQueueNumber : {}", id);
        return findByFoodJoint_Id(id).size();
    }

    /**
     *  Get one ticket by id.
     *
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Ticket getNextTicket(Long foodJointId, Collection<TicketStatus> statuses) {
        log.debug("Request to get getNextTicket");
        return ticketRepository.findTopByFoodJoint_IdAndStatusIn(foodJointId, statuses);
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

    public Ticket getNewTicket(FoodJoint foodJoint, String userName) {
        Ticket ticket = new Ticket();
        ZonedDateTime zonedDateTime = getEstimateWaitingTime(foodJoint);
        ticket.setEstimateTime(zonedDateTime);
        ticket.setNumber(TicketCounterService.getNextTicketNumber(foodJoint.getName()));
        byte[] stream = QRCode.from(foodJoint.getName() + "-" + ticket.getNumber()).stream().toByteArray();
        ticket.setFoodJoint(foodJoint);
        ticket.setQrCode(stream);
        ticket.setQrCodeContentType("image/png");
        ticket.setCreateTime(ZonedDateTime.from(new Date().toInstant().atZone(ZoneId.systemDefault())));
        if (userName != null && !userName.isEmpty()) {
            User user = userRepository.findOneByLogin(userName).get();
            ticket.setUser(user);
        }
        return ticket;
    }

    public ZonedDateTime getEstimateWaitingTime(FoodJoint foodJoint) {
        int waitingNum = getQueueNumber(foodJoint.getId());
        long now = new Date().getTime();
        long waitTime = now + (long) (foodJoint.getEstimatWaitPerPerson() * 1000 * waitingNum);
        Date d = new Date(waitTime);
        return d.toInstant().atZone(ZoneId.systemDefault());
    }
}
