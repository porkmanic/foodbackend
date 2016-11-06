package com.intelli5.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FoodJoint.
 */
@Entity
@Table(name = "food_joint")
public class FoodJoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "serving_number")
    private Long servingNumber;

    @Column(name = "last_issued_ticket_num")
    private Long lastIssuedTicketNum;

    @Column(name = "estimat_wait_per_person")
    private Float estimatWaitPerPerson;

    @OneToMany(mappedBy = "foodJoint")
    @JsonIgnore
    private Set<Ticket> orders = new HashSet<>();

    @OneToMany(mappedBy = "foodJoint")
    @JsonIgnore
    private Set<MenuItem> menuItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public FoodJoint name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public FoodJoint imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getServingNumber() {
        return servingNumber;
    }

    public FoodJoint servingNumber(Long servingNumber) {
        this.servingNumber = servingNumber;
        return this;
    }

    public void setServingNumber(Long servingNumber) {
        this.servingNumber = servingNumber;
    }

    public Long getLastIssuedTicketNum() {
        return lastIssuedTicketNum;
    }

    public FoodJoint lastIssuedTicketNum(Long lastIssuedTicketNum) {
        this.lastIssuedTicketNum = lastIssuedTicketNum;
        return this;
    }

    public void setLastIssuedTicketNum(Long lastIssuedTicketNum) {
        this.lastIssuedTicketNum = lastIssuedTicketNum;
    }

    public Float getEstimatWaitPerPerson() {
        return estimatWaitPerPerson;
    }

    public FoodJoint estimatWaitPerPerson(Float estimatWaitPerPerson) {
        this.estimatWaitPerPerson = estimatWaitPerPerson;
        return this;
    }

    public void setEstimatWaitPerPerson(Float estimatWaitPerPerson) {
        this.estimatWaitPerPerson = estimatWaitPerPerson;
    }

    public Set<Ticket> getOrders() {
        return orders;
    }

    public FoodJoint orders(Set<Ticket> tickets) {
        this.orders = tickets;
        return this;
    }

    public FoodJoint addOrder(Ticket ticket) {
        orders.add(ticket);
        ticket.setFoodJoint(this);
        return this;
    }

    public FoodJoint removeOrder(Ticket ticket) {
        orders.remove(ticket);
        ticket.setFoodJoint(null);
        return this;
    }

    public void setOrders(Set<Ticket> tickets) {
        this.orders = tickets;
    }

    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }

    public FoodJoint menuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
        return this;
    }

    public FoodJoint addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        menuItem.setFoodJoint(this);
        return this;
    }

    public FoodJoint removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);
        menuItem.setFoodJoint(null);
        return this;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoodJoint foodJoint = (FoodJoint) o;
        if(foodJoint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, foodJoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FoodJoint{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", imageUrl='" + imageUrl + "'" +
            ", servingNumber='" + servingNumber + "'" +
            ", lastIssuedTicketNum='" + lastIssuedTicketNum + "'" +
            ", estimatWaitPerPerson='" + estimatWaitPerPerson + "'" +
            '}';
    }
}
