package com.example.orderservicesb.rest;

import com.example.orderservicesb.model.PurchaseOrder;
import com.example.orderservicesb.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderResource {
    
    @Autowired
    OrderService orderService;

    @PostMapping("/orders")
    public OrderOperationResponse addOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        PurchaseOrder order = createOrderRequest.toOrder();
        order = orderService.addOrder(order);
        return OrderOperationResponse.from(order);
    }

    @PutMapping("/{orderId}/lines/{orderLineId}")
    public OrderOperationResponse updateOrderLine(@PathVariable("orderId") long orderId, 
            @PathVariable("orderLineId") long orderLineId, @RequestBody UpdateOrderLineRequest request) {
        PurchaseOrder updated = orderService.updateOrderLine(orderId, orderLineId, request.getNewStatus());
        return OrderOperationResponse.from(updated);
    }

}
