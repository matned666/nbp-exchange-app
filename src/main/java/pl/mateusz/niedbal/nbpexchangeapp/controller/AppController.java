package pl.mateusz.niedbal.nbpexchangeapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;
import pl.mateusz.niedbal.nbpexchangeapp.service.CurrencyService;

import java.math.BigDecimal;

@RestController
public class AppController {

    private final CurrencyService currencyService;

    public AppController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/convert/{amount}")
    public String convertAmount(@PathVariable BigDecimal amount,
                                @RequestParam(defaultValue = "") String date,
                                @RequestParam(defaultValue = "PLN") String currencyA,
                                @RequestParam(defaultValue = "EUR") String currencyB) {
        BigDecimal exchange = currencyService.exchange(amount, currencyA, currencyB, date);
        return "Exchange on " + date + ": " + amount + " " + currencyA.toUpperCase() + " = " + exchange + " " + currencyB.toUpperCase();
    }

}
