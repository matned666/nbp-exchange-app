package pl.mateusz.niedbal.nbpexchangeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Logger;

/**
 * Exception that appears when we have any problem parsing currency rate json from API
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class CurrencyFormatException extends RuntimeException{
    private static final Logger logger = Logger.getLogger(CurrencyFormatException.class.getName());
    public static final String MESSAGE = "Currency has been not found";

    public CurrencyFormatException(String code, String date) {
        super(MESSAGE);
        logger.warning(MESSAGE + code + " >> " + date );
    }
}
