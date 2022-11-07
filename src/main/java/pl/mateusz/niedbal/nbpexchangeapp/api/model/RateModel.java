package pl.mateusz.niedbal.nbpexchangeapp.api.model;

import java.util.Objects;

/**
 * Rate API model: <a href="http://api.nbp.pl/en.html">http://api.nbp.pl/en.html</a> <br>
 * Example: {"no":"214/A/NBP/2022","effectiveDate":"2022-11-04","mid":4.6898}
 */
public class RateModel {

    private String no;
    private String effectiveDate;
    private String mid;

    public RateModel() {
    }

    public RateModel(String no, String effectiveDate, String mid) {
        this.no = no;
        this.effectiveDate = effectiveDate;
        this.mid = mid;
    }

    public String getNo() {
        return no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getMid() {
        return mid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateModel)) return false;
        RateModel rateModel = (RateModel) o;
        return Objects.equals(no, rateModel.no) && Objects.equals(effectiveDate, rateModel.effectiveDate) && Objects.equals(mid, rateModel.mid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, effectiveDate, mid);
    }

    @Override
    public String toString() {
        return "RateModel{" +
                "no='" + no + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", mid='" + mid + '\'' +
                '}';
    }
}
