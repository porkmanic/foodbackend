package com.intelli5.back.service.dto;

/**
 * Created by fhliu on 2016/11/13.
 */
public class ItemDTO {
    private Long id;
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
