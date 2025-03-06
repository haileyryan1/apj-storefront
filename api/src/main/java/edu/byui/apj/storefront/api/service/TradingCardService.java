package edu.byui.apj.storefront.api.service;

import edu.byui.apj.storefront.api.model.TradingCard;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.io.InputStreamReader;

@Service
public class TradingCardService {
    private List<TradingCard> tradingCards = new ArrayList<>();

    @PostConstruct
    public void loadCards() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("pioneers.csv")
        ))) {
            reader.lines().skip(1).forEach(line -> {
                String[] values = line.split(",");
                tradingCards.add(new TradingCard(
                        Long.parseLong(values[0]),
                        values[1],
                        values[2],
                        values[3],
                        new BigDecimal(values[4]),
                        values[5]
                ));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TradingCard> getPageCards(int page, int size){
        int fromIndex = page * size;
        int  toIndex = Math.min(fromIndex + size, tradingCards.size());
        return tradingCards.subList(fromIndex, toIndex);
    }

    public List<TradingCard> filterAndSort(BigDecimal minPrice, BigDecimal maxPrice, String specialty, String sort) {
        return tradingCards.stream()
                .filter(card -> (minPrice == null || card.getPrice().compareTo(minPrice) >= 0) &&
                        (maxPrice == null || card.getPrice().compareTo(maxPrice) <= 0) &&
                        (specialty == null || card.getSpecialty().equalsIgnoreCase(specialty)))
                .sorted(sort.equals("price")
                        ? Comparator.comparing(TradingCard::getPrice)
                        : Comparator.comparing(TradingCard::getName))
                .collect(Collectors.toList());
    }


    public List<TradingCard> searchByQuery(String query) {
        return tradingCards.stream()
                .filter(card -> card.getName().toLowerCase().contains(query.toLowerCase()) ||
                        card.getContribution().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
