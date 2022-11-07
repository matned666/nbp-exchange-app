package pl.mateusz.niedbal.nbpexchangeapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import pl.mateusz.niedbal.nbpexchangeapp.api.ApiRequestService;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.RateModel;
import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;
import pl.mateusz.niedbal.nbpexchangeapp.entity.Currency;
import pl.mateusz.niedbal.nbpexchangeapp.exception.CurrencyNotFoundException;
import pl.mateusz.niedbal.nbpexchangeapp.repository.CurrencyRepository;
import pl.mateusz.niedbal.nbpexchangeapp.utils.DateUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private ApiRequestService apiRequestService;

    @Value("${api.currency.rate.url}")
    private String apiUrl;


    @ParameterizedTest
    @CsvSource({
            "100, 20.00, USD, 1, EUR, 5, 2022-02-02",
            "100, 20.00, PLN, 1, EUR, 5, 2022-02-02",
            "100, 10.00, PLN, 1, EUR, 10, 2022-02-02",
            "100, 66.67, USD, 10, kjhlh, 15, 2022-02-02",
            "100, 6.67, PLN, 1000000, EUR, 15, 2022-02-02",
            "100, 6.67, PLN, 34, EUR, 15, 2022-02-02",
            "100, 20.00, USD, 1, EUR, 5, 2022-02-02"
    })
    void ShouldReturnExpectedResultString_WhenConvertGetRequest(String value, String expectedResultStr, String rateCodeA, String rateA, String rateCodeB, String rateB, String rateDate) throws Exception {
        CurrencyModel currencyModelA = new CurrencyModel("test", rateCodeA, Collections.singletonList(new RateModel(rateDate, rateA)));
        CurrencyModel currencyModelB = new CurrencyModel("test", rateCodeB, Collections.singletonList(new RateModel(rateDate, rateB)));
        Currency currencyA = CurrencyDTO.applyApiModel(currencyModelA).convertToEntity();
        Currency currencyB = CurrencyDTO.applyApiModel(currencyModelB).convertToEntity();

        doReturn(Optional.of(currencyA)).when(currencyRepository).findByCode(rateCodeA, LocalDate.from(DateUtils.formatter.parse(rateDate)));
        doReturn(Optional.of(currencyB)).when(currencyRepository).findByCode(rateCodeB, LocalDate.from(DateUtils.formatter.parse(rateDate)));
        doReturn(currencyA).when(currencyRepository).save(currencyA);
        doReturn(currencyB).when(currencyRepository).save(currencyB);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/convert/"+value+"?currencyA="+rateCodeA+"&currencyB="+rateCodeB+"&date="+rateDate)
                                .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Exchange on "+rateDate+": "+value+" "+rateCodeA.toUpperCase()+" = "+expectedResultStr+" "+rateCodeB.toUpperCase()))
                .andReturn();
    }

    @Test
    void ShouldResult404_WhenWrongCodeGiven() throws Exception {
        String value = "1";
        String notExistingCode = "notExistingCode";
        doReturn(Optional.empty()).when(currencyRepository).findByCode(any(), any());
        doThrow(new CurrencyNotFoundException()).when(apiRequestService).receiveCurrencyModel(any(), any());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/convert/"+value+"?currencyA="+notExistingCode)
                                .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

}