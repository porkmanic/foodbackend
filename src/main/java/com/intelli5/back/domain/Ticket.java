package com.intelli5.back.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.intelli5.back.domain.enumeration.TicketStatus;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Document(indexName = "ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Lob
    @Column(name = "qr_code")
    private byte[] qrCode;

    @Column(name = "qr_code_content_type")
    private String qrCodeContentType;

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

    public Integer getNumber() {
        return number;
    }

    public Ticket number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public Ticket qrCode(byte[] qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeContentType() {
        return qrCodeContentType;
    }

    public Ticket qrCodeContentType(String qrCodeContentType) {
        this.qrCodeContentType = qrCodeContentType;
        return this;
    }

    public void setQrCodeContentType(String qrCodeContentType) {
        this.qrCodeContentType = qrCodeContentType;
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
            ", number='" + number + "'" +
            ", qrCode='" + qrCode + "'" +
            ", qrCodeContentType='" + qrCodeContentType + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
