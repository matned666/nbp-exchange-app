package pl.mateusz.niedbal.nbpexchangeapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;
    private String name;
    private LocalDate date;
    private BigDecimal midRate;

    public Currency() {
    }

    public Currency(String code, String name, LocalDate date, BigDecimal midRate) {
        this.code = code;
        this.name = name;
        this.date = date;
        this.midRate = midRate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        if (!(o instanceof Currency)) return false;
        Currency currency = (Currency) o;
        return id == currency.id && Objects.equals(code, currency.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }
}
