package pl.mateusz.niedbal.nbpexchangeapp.service;

import org.springframework.stereotype.Service;
import pl.mateusz.niedbal.nbpexchangeapp.api.ApiRequestService;
import pl.mateusz.niedbal.nbpexchangeapp.api.model.CurrencyModel;
import pl.mateusz.niedbal.nbpexchangeapp.dto.CurrencyDTO;
import pl.mateusz.niedbal.nbpexchangeapp.entity.Currency;
import pl.mateusz.niedbal.nbpexchangeapp.repository.CurrencyRepository;
import pl.mateusz.niedbal.nbpexchangeapp.utils.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ApiRequestService apiRequestService;


    public CurrencyService(CurrencyRepository currencyRepository, ApiRequestService apiRequestService) {
        this.currencyRepository = currencyRepository;
        this.apiRequestService = apiRequestService;
    }

    public BigDecimal exchange(BigDecimal amount, String codeA, String codeB, String date) {
        CurrencyDTO currencyA = findCurrencyByCodeAndDate(codeA, date);
        CurrencyDTO currencyB = findCurrencyByCodeAndDate(codeB, date);
        BigDecimal currencyABigDecimal = currencyA.getMidRate();
        BigDecimal currencyBBigDecimal = currencyB.getMidRate();
        return amount.multiply(currencyABigDecimal).divide(currencyBBigDecimal,  2, RoundingMode.HALF_UP);
    }

    private CurrencyDTO findCurrencyByCodeAndDate(String code, String date) {
        if (code.equalsIgnoreCase("pln")) {
            return CurrencyDTO.PLN;
        }
        LocalDate searchedDate = LocalDate.from(date.isEmpty()?
                LocalDate.now():
                DateUtils.formatter.parse(date));
        Optional<Currency> currency = currencyRepository.findByCode(code, searchedDate);
        if (currency.isEmpty()) {
            return getCurrencyDTOFromApi(code, date);
        }
        return CurrencyDTO.applyEntity(currency.get());
    }

    private CurrencyDTO getCurrencyDTOFromApi(String code, String date) {
        date = date.isEmpty()?"today": date;
        CurrencyModel currencyModel = apiRequestService.get("http://api.nbp.pl/api/exchangerates/rates/a/"+ code +"/"+ date +"/");
        CurrencyDTO currencyDTO = currencyModel == null ?
                null :
                CurrencyDTO.applyApiModel(currencyModel);
        if (currencyModel == null) throw new RuntimeException("No currencyFound");
        return CurrencyDTO.applyEntity(currencyRepository.save(currencyDTO.convertToEntity()));
    }

}
