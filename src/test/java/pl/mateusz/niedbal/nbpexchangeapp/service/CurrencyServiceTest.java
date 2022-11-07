package pl.mateusz.niedbal.nbpexchangeapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.mateusz.niedbal.nbpexchangeapp.api.ApiRequestService;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.RateModel;
import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;
import pl.mateusz.niedbal.nbpexchangeapp.entity.Currency;
import pl.mateusz.niedbal.nbpexchangeapp.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private ApiRequestService apiRequestService;


    @Test
    void ShouldReturnCurrencyFomApi_WhenNoCurrencyOnRepository(){
        CurrencyModel currencyModel = new CurrencyModel("dolar", "usd", Collections.singletonList(new RateModel("2022-02-02", "5")));
        Currency currency = new Currency("usd", "dolar", LocalDate.of(2022, 2, 2), new BigDecimal(5));

        doReturn(currencyModel).when(apiRequestService).get(any());
        doReturn(Optional.empty()).when(currencyRepository).findByCode("usd", LocalDate.of(2022, 2, 2));
        doReturn(currency).when(currencyRepository).save(any());

        BigDecimal actualResult = currencyService.exchange(new BigDecimal(100), "pln", "usd", "2022-02-02");
        BigDecimal expectedResult = new BigDecimal(20).divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);

        assertEquals(expectedResult, actualResult);
    }








}