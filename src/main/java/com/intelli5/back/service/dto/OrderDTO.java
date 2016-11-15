package com.intelli5.back.service.dto;

import java.util.List;

/**
 * Created by fhliu on 2016/11/13.
 */
public class OrderDTO {
    private String paymentInfo;
    private Long foodJointId;
    private List<ItemDTO> items;

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
}
