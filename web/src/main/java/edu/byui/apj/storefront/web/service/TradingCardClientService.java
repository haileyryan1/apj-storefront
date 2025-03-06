package edu.byui.apj.storefront.web.service;

import edu.byui.apj.storefront.web.model.TradingCard;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TradingCardClientService {
    private final WebClient webClient = WebClient.create("http://localhost:8081/api/cards");

    public List<TradingCard> getAllPageCards(int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("page", page).queryParam("size", size).build())
                .retrieve()
                .bodyToFlux(TradingCard.class)
                .collectList()
                .block();
    }

    public List<TradingCard> filterAndSort(BigDecimal minPrice, BigDecimal maxPrice, String specialty, String sort) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/filter")
                        .queryParamIfPresent("minPrice", minPrice == null ? null : java.util.Optional.of(minPrice))
                        .queryParamIfPresent("maxPrice", maxPrice == null ? null : java.util.Optional.of(maxPrice))
                        .queryParamIfPresent("specialty", specialty == null ? null : java.util.Optional.of(specialty))
                        .queryParam("sort", sort)
                        .build())
                .retrieve()
                .bodyToFlux(TradingCard.class)
                .collectList()
                .block();
    }

    public List<TradingCard> searchByNameOrContribution(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search").queryParam("query", query).build())
                .retrieve()
                .bodyToFlux(TradingCard.class)
                .collectList()
                .block();
    }
}

