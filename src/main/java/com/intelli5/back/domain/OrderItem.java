package com.intelli5.back.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@Document(indexName = "orderitem")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    private MenuItem menuItem;

    @ManyToOne
    private FoodOrder foodOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public OrderItem menuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        return this;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public OrderItem foodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
        return this;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        if(orderItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + id +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
