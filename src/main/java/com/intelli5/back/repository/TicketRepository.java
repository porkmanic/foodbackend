package com.intelli5.back.repository;

import com.intelli5.back.domain.Ticket;

import com.intelli5.back.domain.enumeration.TicketStatus;
import org.springframework.data.jpa.repository.*;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Ticket entity.
 */
@SuppressWarnings("unused")
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("select ticket from Ticket ticket where ticket.user.login = ?#{principal.username}")
    List<Ticket> findByUserIsCurrentUser();
    List<Ticket> findByFoodJoint_Id(Long id);
    List<Ticket> findByFoodJoint_IdAndStatus(Long id, TicketStatus status);
    Ticket findTopByFoodJoint_IdAndStatusIn(Long foodJointId, Collection<TicketStatus> statuses);

}
