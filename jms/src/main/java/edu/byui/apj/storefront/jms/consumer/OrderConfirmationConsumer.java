package edu.byui.apj.storefront.jms.consumer;

import edu.byui.apj.storefront.model.CardOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class OrderConfirmationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfirmationConsumer.class);
    private static final String BASE_URL = "http://localhost:8083";

    private final WebClient webClient;

    public OrderConfirmationConsumer(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    @JmsListener(destination = "orderQueue")
    public void receiveOrderConfirmation(String orderId) {
        try {
            CardOrder order = webClient.get()
                    .uri("/order/{orderId}", orderId)
                    .retrieve()
                    .bodyToMono(CardOrder.class)
                    .block(); // Block is acceptable here for simplicity in a listener context

            logger.info("Order received from DB service: {}", order);

        } catch (WebClientResponseException ex) {
            logger.error("Error response from DB service for orderId {}: {}", orderId, ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            logger.error("Failed to retrieve or process order for orderId {}", orderId, ex);
        }
    }
}
