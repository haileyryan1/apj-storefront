package edu.byui.apj.storefront.jms.controller;

import edu.byui.apj.storefront.jms.producer.OrderConfirmationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm")
public class OrderConfirmationController {
    private final OrderConfirmationProducer producer;

    @Autowired
    public OrderConfirmationController(OrderConfirmationProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable String orderId) {
        try {
            producer.sendOrderConfirmation(orderId);
            String message = "Order confirm message sent for order ID: " + orderId;
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order confirmation message for order ID: " + orderId, e);
        }
    }
}
