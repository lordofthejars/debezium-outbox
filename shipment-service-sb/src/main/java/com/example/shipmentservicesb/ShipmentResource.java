package com.example.shipmentservicesb;

import org.springframework.web.bind.annotation.RestController;


@RestController
public class ShipmentResource {
    

    private ShipmentConsumer shipmentConsumer;

    public ShipmentResource(ShipmentConsumer shipmentConsumer) {
        this.shipmentConsumer = shipmentConsumer;
    }


}
