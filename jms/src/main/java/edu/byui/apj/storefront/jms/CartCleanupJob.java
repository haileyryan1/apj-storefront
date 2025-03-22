package edu.byui.apj.storefront.jms;

import edu.byui.apj.storefront.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CartCleanupJob {

    private static final Logger logger = LoggerFactory.getLogger(CartCleanupJob.class);
    private static final String BASE_URL = "http://localhost:8083";

    private final WebClient webClient;

    public CartCleanupJob(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public List<Cart> getCartsWithoutOrders() {
        try {
            List<Cart> carts = webClient.get()
                    .uri("/cart/noorderLinks")
                    .retrieve()
                    .bodyToFlux(Cart.class)
                    .collectList()
                    .block(); // blocking is OK in a scheduled job

            logger.info("Retrieved {} carts without orders.", carts != null ? carts.size() : 0);
            return carts;
        } catch (WebClientResponseException ex) {
            logger.error("HTTP error while retrieving carts: {}", ex.getResponseBodyAsString(), ex);
            throw new RuntimeException("Failed to retrieve carts without orders", ex);
        } catch (Exception ex) {
            logger.error("Error retrieving carts without orders", ex);
            throw new RuntimeException("Failed to retrieve carts without orders", ex);
        }
    }

    public void cleanupCart(String cartId) {
        try {
            webClient.delete()
                    .uri("/cart/{cartId}", cartId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            logger.info("Successfully deleted cart with ID: {}", cartId);
        } catch (WebClientResponseException ex) {
            logger.error("Failed to delete cart ID {}. Status: {}, Body: {}", cartId, ex.getStatusCode(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            logger.error("Unexpected error deleting cart ID {}", cartId, ex);
        }
    }

    @Scheduled(cron = "0 0 2 * * *") // Runs daily at 2 AM
    public void cleanupCarts() {
        logger.info("Starting cart cleanup job...");

        List<Cart> carts = getCartsWithoutOrders();
        if (carts == null || carts.isEmpty()) {
            logger.info("No carts to clean up.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (Cart cart : carts) {
            executor.submit(() -> cleanupCart(cart.getId()));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for all tasks to finish
        }

        logger.info("Cart cleanup complete.");
    }
}

