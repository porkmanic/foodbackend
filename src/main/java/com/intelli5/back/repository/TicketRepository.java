package com.intelli5.back.repository;

import com.intelli5.back.domain.Ticket;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ticket entity.
 */
@SuppressWarnings("unused")
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("select ticket from Ticket ticket where ticket.user.login = ?#{principal.username}")
    List<Ticket> findByUserIsCurrentUser();

}
