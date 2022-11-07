package pl.mateusz.niedbal.nbpexchangeapp.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Currency API model: <a href="http://api.nbp.pl/en.html">http://api.nbp.pl/en.html</a> <br>
 * Example: {"table":"A","currency":"euro","code":"EUR","rates":[{ ... }]}
 */
public class CurrencyModel {

    private String table;
    private String currency;
    private String code;
    private List<RateModel> rates = new ArrayList<>();

    public CurrencyModel() {
    }

    public CurrencyModel(String currency, String code, List<RateModel> rates) {
        this.table = "A";
        this.currency = currency;
        this.code = code;
        this.rates = rates;
    }

    public String getTable() {
        return table;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyModel)) return false;
        CurrencyModel that = (CurrencyModel) o;
        return Objects.equals(table, that.table) && Objects.equals(currency, that.currency) && Objects.equals(code, that.code) && Objects.equals(rates, that.rates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, currency, code, rates);
    }

    @Override
    public String toString() {
        return "CurrencyModel{" +
                "table='" + table + '\'' +
                ", currency='" + currency + '\'' +
                ", code='" + code + '\'' +
                ", rates=" + rates +
                '}';
    }
}
