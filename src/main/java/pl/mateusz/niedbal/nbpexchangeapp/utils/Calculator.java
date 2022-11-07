package pl.mateusz.niedbal.nbpexchangeapp.utils;

import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {

    private static final int DIGITS_ROUND = 2; // DO NOT CHANGE -> changing this value may cause some tests to fail

    /**
     * Exchanges value from currencyA to currencyB <br>
     * Formula: amount * rateA / rateB
     * @param amount BigDecimal
     * @param currencyA CurrencyDTO
     * @param currencyB CurrencyDTO
     * @return exchanged BigDecimal value
     */
    public static BigDecimal exchange(BigDecimal amount, CurrencyDTO currencyA, CurrencyDTO currencyB) {
        BigDecimal currencyRateA = currencyA.getMidRate();
        BigDecimal currencyRateB = currencyB.getMidRate();
        return amount.multiply(currencyRateA).divide(currencyRateB, DIGITS_ROUND, RoundingMode.HALF_UP);
    }
}
