package com.neoflex.deal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Глобальный обработчик исключений, обрабатывающий различные типы ошибок и возвращающий соответствующие HTTP-статусы.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик исключений, связанных с отсутствием ресурса (например, заявка не найдена).
     * Возвращает HTTP статус 404 NOT FOUND с сообщением об ошибке.
     *
     * @param ex исключение, содержащее сообщение об ошибке
     * @return ответ с сообщением об ошибке и статусом 404
     */
    @ExceptionHandler({
            StatementNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
