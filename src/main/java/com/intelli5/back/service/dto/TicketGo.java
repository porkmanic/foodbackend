package com.intelli5.back.service.dto;

import com.intelli5.back.domain.FoodJoint;
import com.intelli5.back.domain.FoodOrder;
import com.intelli5.back.domain.Ticket;
import com.intelli5.back.domain.User;
import com.intelli5.back.domain.enumeration.TicketStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fhliu on 2016/12/2.
 */
public class TicketGo {
    private Long id;
    private Integer number;
    private byte[] qrCode;
    private String qrCodeContentType;
    private TicketStatus status;
    private String createTime;
    private int estimateTime;
    private FoodOrder foodOrder;
    private User user;
    private FoodJoint foodJoint;

    public TicketGo(Ticket ticket) {
        this.id = ticket.getId();
        this.number = ticket.getNumber();
        this.qrCode = ticket.getQrCode();
        this.qrCodeContentType = ticket.getQrCodeContentType();
        this.status = ticket.getStatus();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (ticket.getCreateTime() != null)
            this.createTime = df.format(Date.from(ticket.getCreateTime().toInstant()));
        this.foodOrder = ticket.getFoodOrder();
        this.user = ticket.getUser();
        this.foodJoint = ticket.getFoodJoint();
    }

    public FoodJoint getFoodJoint() {
        return foodJoint;
    }

    public void setFoodJoint(FoodJoint foodJoint) {
        this.foodJoint = foodJoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeContentType() {
        return qrCodeContentType;
    }

    public void setQrCodeContentType(String qrCodeContentType) {
        this.qrCodeContentType = qrCodeContentType;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(int estimateTime) {
        this.estimateTime = estimateTime;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
