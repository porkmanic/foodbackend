package com.intelli5.back.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A MenuItem.
 */
@Entity
@Table(name = "menu_item")
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    private FoodJoint foodJoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MenuItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuItem price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MenuItem imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FoodJoint getFoodJoint() {
        return foodJoint;
    }

    public MenuItem foodJoint(FoodJoint foodJoint) {
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
        MenuItem menuItem = (MenuItem) o;
        if(menuItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, menuItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", price='" + price + "'" +
            ", imageUrl='" + imageUrl + "'" +
            '}';
    }
}
