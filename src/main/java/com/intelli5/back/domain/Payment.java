package com.intelli5.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.intelli5.back.domain.enumeration.PayStatus;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "total_price", precision=10, scale=2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PayStatus status;

    @Column(name = "payment_info")
    private String paymentInfo;

    @OneToOne(mappedBy = "payment")
    @JsonIgnore
    private FoodOrder foodOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Payment totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PayStatus getStatus() {
        return status;
    }

    public Payment status(PayStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(PayStatus status) {
        this.status = status;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public Payment paymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
        return this;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public Payment foodOrder(FoodOrder foodOrder) {
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
        Payment payment = (Payment) o;
        if(payment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + id +
            ", totalPrice='" + totalPrice + "'" +
            ", status='" + status + "'" +
            ", paymentInfo='" + paymentInfo + "'" +
            '}';
    }
}
