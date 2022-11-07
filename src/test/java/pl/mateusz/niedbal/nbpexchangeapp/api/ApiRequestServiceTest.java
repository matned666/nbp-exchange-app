package pl.mateusz.niedbal.nbpexchangeapp.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.mateusz.niedbal.nbpexchangeapp.exception.CurrencyNotFoundException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class ApiRequestServiceTest {

    @Autowired
    private ApiRequestService apiRequestService;

    @Test
    void ShouldThrowCurrencyNotFoundException_WhenWrongCodeGiven() throws IOException{
            URL url = new URL("http://api.nbp.pl");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
//             First checks API connection. If it fails there is no test assert
            if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
                assertThrows(CurrencyNotFoundException.class, () ->
                        apiRequestService.receiveCurrencyModel("notExistingCode", "noDate"));
            }
    }

}