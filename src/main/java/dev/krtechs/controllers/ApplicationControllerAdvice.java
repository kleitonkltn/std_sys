package dev.krtechs.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import dev.krtechs.core.exception.ApiErrors;
import dev.krtechs.core.exception.CustomErrorResponse;



@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationErrors(final MethodArgumentNotValidException exception) {
        final BindingResult bindingResult = exception.getBindingResult();
        final List<String> messages = bindingResult.getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage()).collect(Collectors.toList());
        return new ApiErrors(messages);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleResponseStatusException(final ResponseStatusException exception) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setErrorCode(exception.getStatus().name());
        error.setErrorMsg(exception.getReason());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus((exception.getStatus().value()));
        return new ResponseEntity<>(error, exception.getStatus());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleGenericBadCredentialsException(BadCredentialsException e) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setErrorCode("BAD_CREDENTIALS_ERROR");
        error.setErrorMsg("INCORRECT CREDENTIALS");
        error.setTimestamp(LocalDateTime.now());
        error.setStatus((HttpStatus.UNAUTHORIZED.value()));
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}