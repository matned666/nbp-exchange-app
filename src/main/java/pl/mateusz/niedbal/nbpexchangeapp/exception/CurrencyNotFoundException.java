package pl.mateusz.niedbal.nbpexchangeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CurrencyNotFoundException extends RuntimeException{

    public CurrencyNotFoundException() {
        super("Currency has been not found");
    }
}
