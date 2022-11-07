package pl.mateusz.niedbal.nbpexchangeapp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.exception.CurrencyFormatException;
import pl.mateusz.niedbal.nbpexchangeapp.exception.CurrencyNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@Component
public class ApiRequestService {
    private static final Logger logger = Logger.getLogger(ApiRequestService.class.getName());

    @Value("${api.currency.rate.url}")
    private String apiUrl;

    public CurrencyModel receiveCurrencyModel(String code, String date) {
        String apiResponse = receiveCurrencyModelApiResp(code, date);
        ObjectMapper mapper = new ObjectMapper();
        try {
            CurrencyModel currencyModel = mapper.readValue(apiResponse, CurrencyModel.class);
            logger.info("Currency " + code + " from " + date + " was taken from remote API.");
            return currencyModel;
        } catch (JsonProcessingException e) {
            throw new CurrencyFormatException(code, date);
        }
    }

    private String receiveCurrencyModelApiResp(String code, String date) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(apiUrl + code +"/"+ date +"/");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line)
                      .append("\n");
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new CurrencyNotFoundException(code, date);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
