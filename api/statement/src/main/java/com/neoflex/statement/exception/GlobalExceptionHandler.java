package com.neoflex.statement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ValidationErrorResponse handleHttpClientErrorExceptionNotFound(org.springframework.web.client.HttpClientErrorException.NotFound e) {
        // Получаем тело ответа с ошибкой от внешнего сервиса
        String errorMessage = e.getResponseBodyAsString();

        // Создаем объект Violation с текстом ошибки из микросервиса
        Violation violation = new Violation("remoteService", errorMessage);

        // Возвращаем его обернутым в ValidationErrorResponse
        return new ValidationErrorResponse(Collections.singletonList(violation));
    }
}
