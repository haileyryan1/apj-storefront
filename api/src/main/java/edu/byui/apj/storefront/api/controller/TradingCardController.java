package edu.byui.apj.storefront.api.controller;

import edu.byui.apj.storefront.api.model.TradingCard;
import edu.byui.apj.storefront.api.service.TradingCardService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class TradingCardController {
    private final TradingCardService service;

    public TradingCardController(TradingCardService service) {
        this.service = service;
    }

    @GetMapping
    public List<TradingCard> getCards(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "0") int size){
        return service.getPageCards(page, size);
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
        return service.searchByQuery(query);
    }
}
