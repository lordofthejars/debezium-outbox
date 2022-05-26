package com.example.orderservicesb.service;

import java.util.Optional;
import java.util.UUID;

import com.example.orderservicesb.model.EntityNotFoundException;
import com.example.orderservicesb.model.OrderLineStatus;
import com.example.orderservicesb.model.PurchaseOrder;
import com.example.orderservicesb.outbox.ExportedEvent;
import com.example.orderservicesb.outbox.InvoiceCreatedEvent;
import com.example.orderservicesb.outbox.OrderCreatedEvent;
import com.example.orderservicesb.outbox.OrderLineUpdatedEvent;
import com.example.orderservicesb.outbox.Outbox;
import com.example.orderservicesb.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    OutboxRepository outboxRepository;

    /**
     * Add a new {@link PurchaseOrder}.
     *
     * @param order the purchase order
     * @return the persisted purchase order
     */
    @Transactional
    public PurchaseOrder addOrder(PurchaseOrder order) {
        order = this.purchaseOrderRepository.save(order);

        // Fire events for newly created PurchaseOrder
        final OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.of(order);
        this.outboxRepository.save(of(orderCreatedEvent));

        final InvoiceCreatedEvent invoiceCreatedEvent = InvoiceCreatedEvent.of(order);
        this.outboxRepository.save(of(invoiceCreatedEvent));

        return order;
    }

    /**
     * Update the a {@link PurchaseOrder} line's status.
     *
     * @param orderId the purchase order id
     * @param orderLineId the purchase order line id
     * @param newStatus the new order line status
     * @return the updated purchase order
     */
    @Transactional
    public PurchaseOrder updateOrderLine(long orderId, long orderLineId, OrderLineStatus newStatus) {
        Optional<PurchaseOrder> order = this.purchaseOrderRepository.findById(orderId);
        if (order.isPresent()) {
            throw new EntityNotFoundException("Order with id " + orderId + " could not be found");
        }

        PurchaseOrder purchaseOrder = order.get();
        OrderLineStatus oldStatus = purchaseOrder.updateOrderLine(orderLineId, newStatus);
        this.purchaseOrderRepository.save(purchaseOrder);
        
        final OrderLineUpdatedEvent orderLineUpdated = OrderLineUpdatedEvent.of(orderId, orderLineId, newStatus, oldStatus);
        this.outboxRepository.save(of(orderLineUpdated));

        return purchaseOrder;
    }

    private static Outbox of(ExportedEvent exportedEvent) {
        Outbox outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setAggregateId(exportedEvent.getAggregateId());
        outbox.setAggregateType(exportedEvent.getAggregateType());
        outbox.setType(exportedEvent.getType());
        outbox.setTimestamp(exportedEvent.getTimestamp());
        try {
            outbox.setPayload(mapper.writeValueAsString(exportedEvent.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return outbox;
    }
}

// https://www.javaguides.net/2019/08/spring-boot-spring-data-jpa-postgresql-example.html
