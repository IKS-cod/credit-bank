package com.neoflex.deal.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;


    @ExceptionHandler(StatementNotFoundException.class)
    public ResponseEntity<ValidationErrorResponse> handleNotFoundException(StatementNotFoundException ex) {
        Violation violation = new Violation("statement", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ValidationErrorResponse(List.of(violation)));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        String details = extractDetailMessage(ex);
        Violation violation = new Violation("email", details);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ValidationErrorResponse(List.of(violation)));
    }


    @ExceptionHandler(org.springframework.web.client.HttpStatusCodeException.class)
    public ResponseEntity<ValidationErrorResponse> handleHttpStatusCode(org.springframework.web.client.HttpStatusCodeException ex) {
        String message = extractExternalServiceMessage(ex);
        Violation violation = new Violation("externalService", message);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ValidationErrorResponse(List.of(violation)));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationErrorResponse> handleAnyException(Exception e) {
        String message = extractDetailMessage(e);
        Violation violation = new Violation("error", message);
        ValidationErrorResponse response = new ValidationErrorResponse(List.of(violation));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    private String extractDetailMessage(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        String msg = root.getMessage();
        if (msg == null) {
            return "Неизвестная ошибка";
        }
        String detailsMarker = "Подробности: ";
        int idx = msg.indexOf(detailsMarker);
        if (idx != -1) {
            return msg.substring(idx + detailsMarker.length()).trim();
        }
        detailsMarker = "Detail: ";
        idx = msg.indexOf(detailsMarker);
        if (idx != -1) {
            return msg.substring(idx + detailsMarker.length()).trim();
        }
        return msg;
    }


    private String extractExternalServiceMessage(org.springframework.web.client.HttpStatusCodeException ex) {
        String responseBody = ex.getResponseBodyAsString();
        if (responseBody == null || responseBody.isBlank()) {
            return ex.getStatusText();
        }
        try {
            Map<?, ?> map = objectMapper.readValue(responseBody, Map.class);
            Object message = map.get("message");
            if (message != null) {
                return message.toString();
            }
            Object error = map.get("error");
            if (error != null) {
                return error.toString();
            }
        } catch (Exception parseEx) {

        }
        return responseBody;
    }
}

