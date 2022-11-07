package pl.mateusz.niedbal.nbpexchangeapp.api.model;

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

    public RateModel(String effectiveDate, String mid) {
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
    public String toString() {
        return "RateModel{" +
                "no='" + no + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", mid='" + mid + '\'' +
                '}';
    }
}
