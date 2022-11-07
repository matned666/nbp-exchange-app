package pl.mateusz.niedbal.nbpexchangeapp.service;

import org.springframework.stereotype.Service;
import pl.mateusz.niedbal.nbpexchangeapp.api.ApiRequestService;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;
import pl.mateusz.niedbal.nbpexchangeapp.entity.Currency;
import pl.mateusz.niedbal.nbpexchangeapp.exception.CurrencyNotFoundException;
import pl.mateusz.niedbal.nbpexchangeapp.repository.CurrencyRepository;
import pl.mateusz.niedbal.nbpexchangeapp.utils.Calculator;
import pl.mateusz.niedbal.nbpexchangeapp.utils.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CurrencyService {
private static final Logger logger = Logger.getLogger(CurrencyService.class.getName());

    private final CurrencyRepository currencyRepository;
    private final ApiRequestService apiRequestService;


    public CurrencyService(CurrencyRepository currencyRepository, ApiRequestService apiRequestService) {
        this.currencyRepository = currencyRepository;
        this.apiRequestService = apiRequestService;
    }

    /**
     * Calculates value exchange from currencyA to currencyB
     * @param amount to exchange
     * @param codeA 3 letters to exchange currency code existing in NBP table A
     * @param codeB 3 letters exchange on currency code existing in NBP table A
     * @param date currency rate date
     * @return BigDecimal currency exchange - amount * currencyRateA / currencyRateB - rounded to 2 digits after a coma
     */
    public BigDecimal exchange(BigDecimal amount, String codeA, String codeB, String date) {
        CurrencyDTO currencyA = findCurrency(codeA, date);
        CurrencyDTO currencyB = findCurrency(codeB, date);
        return Calculator.exchange(amount, currencyA, currencyB);
    }

    private CurrencyDTO findCurrency(String code, String date) {
        if (code.equalsIgnoreCase("pln")) {
            logger.info("Generated PLN reference currency with rate 1");
            return CurrencyDTO.PLN;
        }
        LocalDate searchedDate = LocalDate.from(date.isEmpty()?
                LocalDate.now():
                DateUtils.formatter.parse(date));
        Optional<Currency> currency = currencyRepository.findByCode(code, searchedDate);
        if (currency.isEmpty()) {
            return getCurrencyDTOFromApi(code, date);
        }
        logger.info("Currency " + code + " from " + (!date.equals("") ? date : "today") + " was taken from local repository.");
        return CurrencyDTO.applyEntity(currency.get());
    }

    private CurrencyDTO getCurrencyDTOFromApi(String code, String date) {
        date = date.isEmpty()?"today": date;
        CurrencyModel currencyModel = apiRequestService.receiveCurrencyModel(code, date);
        CurrencyDTO currencyDTO = currencyModel == null ?
                null :
                CurrencyDTO.applyApiModel(currencyModel);
        if (currencyModel == null) throw new CurrencyNotFoundException(code, date);
        return CurrencyDTO.applyEntity(currencyRepository.save(currencyDTO.convertToEntity()));
    }

}
