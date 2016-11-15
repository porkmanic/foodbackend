package com.intelli5.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

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
@Document(indexName = "foodjoint")
public class FoodJoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "working_hours")
    private String workingHours;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

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

    public String getWorkingHours() {
        return workingHours;
    }

    public FoodJoint workingHours(String workingHours) {
        this.workingHours = workingHours;
        return this;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public byte[] getImage() {
        return image;
    }

    public FoodJoint image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public FoodJoint imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
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
            ", workingHours='" + workingHours + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", estimatWaitPerPerson='" + estimatWaitPerPerson + "'" +
            '}';
    }
}
