package com.intelli5.back.service.dto;

import java.util.List;

/**
 * Created by fhliu on 2016/11/13.
 */
public class OrderDTO {
    private String paymentInfo;
    private Long foodJointId;
    private List<ItemDTO> items;
    private Long userId;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public Long getFoodJointId() {
        return foodJointId;
    }

    public void setFoodJointId(Long foodJointId) {
        this.foodJointId = foodJointId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
