package com.example.orderservicesb.service;

import com.example.orderservicesb.model.PurchaseOrder;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {
    
}
