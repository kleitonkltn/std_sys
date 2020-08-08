package dev.krtechs.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DateFormatterException extends RuntimeException{
    private static final long serialVersionUID = 5285788346619184167L;

    public DateFormatterException(String Mask) {
        super("Date Mask/Formatter Incorrect - Mask Example("+Mask+")");
    }
}
