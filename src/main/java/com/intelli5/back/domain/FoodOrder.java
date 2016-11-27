package com.intelli5.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A FoodOrder.
 */
@Entity
@Table(name = "food_order")
@Document(indexName = "foodorder")
public class FoodOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "total_price", precision=10, scale=2)
    private BigDecimal totalPrice;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(unique = true)
    private Payment payment;

    @OneToMany(mappedBy = "foodOrder",cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne(mappedBy = "foodOrder",cascade = CascadeType.ALL)
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

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public FoodOrder totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public FoodOrder payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
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

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public FoodOrder ticket(Ticket ticket) {
        this.ticket = ticket;
        return this;
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
