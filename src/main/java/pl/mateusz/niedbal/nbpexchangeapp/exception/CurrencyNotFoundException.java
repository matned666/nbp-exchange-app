package pl.mateusz.niedbal.nbpexchangeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Logger;

/**
 * Exception that appears when we have any problem with loading currency rate from API
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CurrencyNotFoundException extends RuntimeException{
    private static final Logger logger = Logger.getLogger(CurrencyNotFoundException.class.getName());

    public static final String MESSAGE = "Currency has been not found: ";

    public CurrencyNotFoundException(String code, String date) {
        super(MESSAGE);
        logger.warning(MESSAGE + code + " >> " + date );
        logger.warning("Exception happens - code 404");
    }
}
