package com.example.orderservicesb.rest;

import com.example.orderservicesb.model.OrderLineStatus;

public class UpdateOrderLineRequest {

    private OrderLineStatus newStatus;

    public OrderLineStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderLineStatus newStatus) {
        this.newStatus = newStatus;
    }
}
