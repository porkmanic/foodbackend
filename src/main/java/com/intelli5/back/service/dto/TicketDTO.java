package com.intelli5.back.service.dto;

import com.intelli5.back.domain.enumeration.TicketStatus;

/**
 * Created by fhliu on 2016/11/26.
 */
public class TicketDTO {
    private Long foodJointId;
    private Long previousTicketId;
    private TicketStatus previousTicketStatus;

    public Long getFoodJointId() {
        return foodJointId;
    }

    public void setFoodJointId(Long foodJointId) {
        this.foodJointId = foodJointId;
    }

    public Long getPreviousTicketId() {
        return previousTicketId;
    }

    public void setPreviousTicketId(Long previousTicketId) {
        this.previousTicketId = previousTicketId;
    }

    public TicketStatus getPreviousTicketStatus() {
        return previousTicketStatus;
    }

    public void setPreviousTicketStatus(TicketStatus previousTicketStatus) {
        this.previousTicketStatus = previousTicketStatus;
    }
}
