package edu.byui.apj.storefront.web.controller;

import edu.byui.apj.storefront.web.model.TradingCard;
import edu.byui.apj.storefront.web.service.TradingCardClientService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/web/cards")
public class TradingCardController {
    private final TradingCardClientService service;

    public TradingCardController(TradingCardClientService service) {
        this.service = service;
    }

    @GetMapping
    public List<TradingCard> getCards(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return service.getAllPageCards(page, size);
    }

    @GetMapping("/filter")
    public List<TradingCard> filterAndSort(@RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false) BigDecimal maxPrice,
                                           @RequestParam(required = false) String specialty,
                                           @RequestParam(defaultValue = "name") String sort) {
        return service.filterAndSort(minPrice, maxPrice, specialty, sort);
    }

    @GetMapping("/search")
    public List<TradingCard> searchCards(@RequestParam String query) {
        return service.searchByNameOrContribution(query);
    }
}
