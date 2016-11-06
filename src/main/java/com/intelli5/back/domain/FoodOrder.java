package com.intelli5.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FoodOrder.
 */
@Entity
@Table(name = "food_order")
public class FoodOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "total_price", precision=10, scale=2)
    private BigDecimal totalPrice;

    @OneToOne
    @JoinColumn(unique = true)
    private Payment payment;

    @OneToMany(mappedBy = "foodOrder")
    @JsonIgnore
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne(mappedBy = "foodOrder")
    @JsonIgnore
    private Ticket ticket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public FoodOrder totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Payment getPayment() {
        return payment;
    }

    public FoodOrder payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public FoodOrder orderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public FoodOrder addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setFoodOrder(this);
        return this;
    }

    public FoodOrder removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setFoodOrder(null);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public FoodOrder ticket(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoodOrder foodOrder = (FoodOrder) o;
        if(foodOrder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, foodOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FoodOrder{" +
            "id=" + id +
            ", totalPrice='" + totalPrice + "'" +
            '}';
    }
}
