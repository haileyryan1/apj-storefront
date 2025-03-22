package edu.byui.apj.storefront.jms.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderConfirmationProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfirmationProducer.class);
    private static final String DESTINATION = "orderQueue";

    private final JmsTemplate jmsTemplate;

    public OrderConfirmationProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendOrderConfirmation(String orderId) {
        jmsTemplate.convertAndSend(DESTINATION, orderId);
        logger.info("JMS message sent to '{}' with payload: {}", DESTINATION, orderId);
    }
}

