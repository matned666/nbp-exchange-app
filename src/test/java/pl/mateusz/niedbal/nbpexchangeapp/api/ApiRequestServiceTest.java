package pl.mateusz.niedbal.nbpexchangeapp.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.RateModel;
import pl.mateusz.niedbal.nbpexchangeapp.exception.CurrencyNotFoundException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class ApiRequestServiceTest {

    @Autowired
    private ApiRequestService apiRequestService;

    /**
     * Request: <br>
     * <a href="https://api.nbp.pl/api/exchangerates/rates/a/usd/2022-11-04/">https://api.nbp.pl/api/exchangerates/rates/a/usd/2022-11-04/</a> <br>
     * <ExchangeRatesSeries xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"> <br>
     * <Table>A</Table> <br>
     * <Currency>dolar amerykański</Currency> <br>
     * <Code>USD</Code> <br>
     * <Rates> <br>
     * <Rate> <br>
     * <No>214/A/NBP/2022</No> <br>
     * <EffectiveDate>2022-11-04</EffectiveDate> <br>
     * <Mid>4.7975</Mid> <br>
     * </Rate> <br>
     * </Rates> <br>
     * </ExchangeRatesSeries> <br>
     * @throws IOException
     */
    @Test
    void ShouldReturnCorrectApiModel_WhenUSDCode20221108DateGiven() throws IOException {
        URL url = new URL("http://api.nbp.pl");
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.connect();
//             First checking API connection.
        assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());

        RateModel rateModel = new RateModel("214/A/NBP/2022", "2022-11-04", "4.7975");
        CurrencyModel currencyModel = new CurrencyModel("dolar amerykański", "USD", Collections.singletonList(rateModel));

        assertEquals(currencyModel, apiRequestService.receiveCurrencyModel("usd", "2022-11-04"));

    }

    @Test
    void ShouldThrowCurrencyNotFoundException_WhenWrongCodeGiven() throws IOException {
        URL url = new URL("http://api.nbp.pl");
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.connect();
//             First checking API connection.
        assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
        assertThrows(CurrencyNotFoundException.class, () ->
                apiRequestService.receiveCurrencyModel("notExistingCode", "noDate"));

    }

}