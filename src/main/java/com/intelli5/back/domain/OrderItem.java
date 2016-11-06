package com.intelli5.back.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "name")
    private String name;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

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

    public String getName() {
        return name;
    }

    public OrderItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderItem price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public OrderItem imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
            ", name='" + name + "'" +
            ", price='" + price + "'" +
            ", imageUrl='" + imageUrl + "'" +
            '}';
    }
}
