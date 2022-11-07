package pl.mateusz.niedbal.nbpexchangeapp.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "100, 1, 5, 20.00",
            "10, 1, 5, 2.00",
            "1, 1, 5, 0.20",
            "1, 5, 5, 1.00",
            "1, 5, 1, 5.00",
            "10, 5, 1, 50.00",
            "100, 5, 1, 500.00",
            "100, 1, 10, 10.00",
            "10, 10, 1, 100.00",
            "100, 10, 15, 66.67",
            "100, 1, 15, 6.67",
    })
    void ShouldReturnExpectedExchangeValue_WhenRatesGiven(String amount, String rateA, String rateB, String expectedResult){
        CurrencyDTO currencyA = new CurrencyDTO(null, null, null, new BigDecimal(rateA));
        CurrencyDTO currencyB = new CurrencyDTO(null, null, null, new BigDecimal(rateB));
        BigDecimal amountBD = new BigDecimal(amount);
        BigDecimal expectedBD = new BigDecimal(expectedResult);

        BigDecimal result = Calculator.exchange(amountBD, currencyA, currencyB);

        assertEquals(expectedBD, result);
    }

}