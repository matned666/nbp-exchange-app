package pl.mateusz.niedbal.nbpexchangeapp.dto;

import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.RateModel;
import pl.mateusz.niedbal.nbpexchangeapp.entity.Currency;
import pl.mateusz.niedbal.nbpexchangeapp.utils.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CurrencyDTO {

    public final static CurrencyDTO PLN = new CurrencyDTO("pln", "złoty", LocalDate.now(), new BigDecimal(1));

    private String code;
    private String name;
    private LocalDate date;
    private BigDecimal midRate;


    public static CurrencyDTO applyEntity(Currency entity) {
        return new CurrencyDTO(entity.getCode(), entity.getName(), entity.getDate(), entity.getMidRate());
    }

    public static CurrencyDTO applyApiModel(CurrencyModel currencyModel) {
        LocalDate date = null;
        List<RateModel> rates = currencyModel.getRates();
        RateModel rate = null;
        if (rates.isEmpty()) {
            throw new RuntimeException("No such rate");
        }
        rates.sort(Comparator.comparing(o -> LocalDate.from(DateUtils.formatter.parse(o.getEffectiveDate()))));
        rate =  rates.get(rates.size()-1);
        if (rate.getEffectiveDate() == null || rate.getMid() == null) {
            throw new RuntimeException("No such rate");
        }
        String effectiveDate = rate.getEffectiveDate();
        date = effectiveDate == null ? null : LocalDate.from(DateUtils.formatter.parse(effectiveDate));
        BigDecimal rateMid = new BigDecimal(rate.getMid());
        return new CurrencyDTO(currencyModel.getCode(), currencyModel.getCurrency(), date, rateMid);
    }

    public CurrencyDTO() {
    }

    public CurrencyDTO(String code, String name, LocalDate date, BigDecimal midRate) {
        this.code = code;
        this.name = name;
        this.date = date;
        this.midRate = midRate;
    }

    public Currency convertToEntity() {
        return new Currency(code, name, date, midRate);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getMidRate() {
        return midRate;
    }

    public void setMidRate(BigDecimal midRate) {
        this.midRate = midRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyDTO)) return false;
        CurrencyDTO that = (CurrencyDTO) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(midRate, that.midRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, date, midRate);
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", midRate=" + midRate +
                '}';
    }
}
