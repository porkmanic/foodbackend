package com.intelli5.back.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.intelli5.back.domain.enumeration.TicketStatus;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketStatus status;

    @OneToOne
    @JoinColumn(unique = true)
    private FoodOrder foodOrder;

    @ManyToOne
    private User user;

    @ManyToOne
    private FoodJoint foodJoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Ticket status(TicketStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public Ticket foodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
        return this;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    public User getUser() {
        return user;
    }

    public Ticket user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FoodJoint getFoodJoint() {
        return foodJoint;
    }

    public Ticket foodJoint(FoodJoint foodJoint) {
        this.foodJoint = foodJoint;
        return this;
    }

    public void setFoodJoint(FoodJoint foodJoint) {
        this.foodJoint = foodJoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        if(ticket.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + id +
            ", status='" + status + "'" +
            '}';
    }
}
