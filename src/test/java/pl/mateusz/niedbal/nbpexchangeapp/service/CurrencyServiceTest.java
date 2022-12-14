package pl.mateusz.niedbal.nbpexchangeapp.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.mateusz.niedbal.nbpexchangeapp.api.ApiRequestService;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.RateModel;
import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;
import pl.mateusz.niedbal.nbpexchangeapp.entity.Currency;
import pl.mateusz.niedbal.nbpexchangeapp.repository.CurrencyRepository;
import pl.mateusz.niedbal.nbpexchangeapp.utils.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

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
            "100, 66.67, USD, 10, EUR, 15, 2022-02-02",
            "100, 6.67, PLN, 1000000, EUR, 15, 2022-02-02",
            "100, 6.67, PLN, 34, EUR, 15, 2022-02-02",
            "100, 20.00, USD, 1, EUR, 5, 2022-02-02"
    })
    void ShouldReturnCurrencyFomApi_WhenNoCurrencyOnRepository(String value, String expectedResultStr, String rateCodeA, String rateA, String rateCodeB, String rateB, String rateDate){
        CurrencyModel currencyModelA = new CurrencyModel("test", rateCodeA, Collections.singletonList(new RateModel("test", rateDate, rateA)));
        CurrencyModel currencyModelB = new CurrencyModel("test", rateCodeB, Collections.singletonList(new RateModel("test", rateDate, rateB)));
        Currency currencyA = CurrencyDTO.applyApiModel(currencyModelA).convertToEntity();
        Currency currencyB = CurrencyDTO.applyApiModel(currencyModelB).convertToEntity();

        doReturn(Optional.empty()).when(currencyRepository).findByCode(rateCodeA, LocalDate.from(DateUtils.formatter.parse(rateDate)));
        doReturn(Optional.empty()).when(currencyRepository).findByCode(rateCodeB, LocalDate.from(DateUtils.formatter.parse(rateDate)));
        doReturn(currencyModelA).when(apiRequestService).receiveCurrencyModel(rateCodeA, rateDate);
        doReturn(currencyModelB).when(apiRequestService).receiveCurrencyModel(rateCodeB, rateDate);
        doReturn(currencyA).when(currencyRepository).save(currencyA);
        doReturn(currencyB).when(currencyRepository).save(currencyB);

        BigDecimal actualResult = currencyService.exchange(new BigDecimal(value), rateCodeA, rateCodeB, rateDate);
        BigDecimal expectedResult = new BigDecimal(expectedResultStr);

        assertEquals(expectedResult, actualResult);
    }


    @ParameterizedTest
    @CsvSource({
            "100, 20.00, USD, 1, EUR, 5, 2022-02-02",
            "100, 20.00, PLN, 1, EUR, 5, 2022-02-02",
            "100, 10.00, PLN, 1, EUR, 10, 2022-02-02",
            "100, 66.67, USD, 10, EUR, 15, 2022-02-02",
            "100, 6.67, PLN, 1000000, EUR, 15, 2022-02-02",
            "100, 6.67, PLN, 34, EUR, 15, 2022-02-02",
            "100, 20.00, USD, 1, EUR, 5, 2022-02-02"
    })
    void ShouldReturnCurrencyFomRepository_WhenFoundOnRepository(String value, String expectedResultStr, String rateCodeA, String rateA, String rateCodeB, String rateB, String rateDate){
        CurrencyModel currencyModelA = new CurrencyModel("test", rateCodeA, Collections.singletonList(new RateModel("test", rateDate, rateA)));
        CurrencyModel currencyModelB = new CurrencyModel("test", rateCodeB, Collections.singletonList(new RateModel("test", rateDate, rateB)));
        Currency currencyA = CurrencyDTO.applyApiModel(currencyModelA).convertToEntity();
        Currency currencyB = CurrencyDTO.applyApiModel(currencyModelB).convertToEntity();

        doReturn(Optional.of(currencyA)).when(currencyRepository).findByCode(rateCodeA, LocalDate.from(DateUtils.formatter.parse(rateDate)));
        doReturn(Optional.of(currencyB)).when(currencyRepository).findByCode(rateCodeB, LocalDate.from(DateUtils.formatter.parse(rateDate)));
        doThrow(RuntimeException.class).when(apiRequestService).receiveCurrencyModel(any(), any());
        doReturn(currencyA).when(currencyRepository).save(currencyA);
        doReturn(currencyB).when(currencyRepository).save(currencyB);

        BigDecimal actualResult = currencyService.exchange(new BigDecimal(value), rateCodeA, rateCodeB, rateDate);
        BigDecimal expectedResult = new BigDecimal(expectedResultStr);

        assertEquals(expectedResult, actualResult);
    }

}