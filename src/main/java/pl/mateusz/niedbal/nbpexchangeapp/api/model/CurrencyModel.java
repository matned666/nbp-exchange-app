package pl.mateusz.niedbal.nbpexchangeapp.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Example: {"table":"A","currency":"euro","code":"EUR","rates":[{ ... }]}
 */
public class CurrencyModel {

    private String currency;
    private String code;
    private List<RateModel> rates = new ArrayList<>();

    public CurrencyModel() {
    }

    public CurrencyModel(String currency, String code, List<RateModel> rates) {
        this.currency = currency;
        this.code = code;
        this.rates = rates;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCode() {
        return code;
    }

    public List<RateModel> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "CurrencyModel{" +
                "currency='" + currency + '\'' +
                ", code='" + code + '\'' +
                ", rates=" + rates +
                '}';
    }
}
