package com.neoflex.deal.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ValidationErrorResponse> handleHttpStatusCode(HttpStatusCodeException ex) {
        String responseBody = ex.getResponseBodyAsString();
        if (responseBody != null && !responseBody.isBlank()) {
            try {
                JsonNode root = objectMapper.readTree(responseBody);

                // violations — приоритетный путь
                JsonNode violationsNode = root.get("violations");
                if (violationsNode != null && violationsNode.isArray() && violationsNode.size() > 0) {
                    System.out.println("[DEBUG] violations node detected, converting to Violation[]");
                    List<Violation> violations = Arrays.asList(
                            objectMapper.treeToValue(violationsNode, Violation[].class)
                    );
                    return ResponseEntity
                            .status(ex.getStatusCode())
                            .body(new ValidationErrorResponse(violations));
                }

                // message
                JsonNode messageNode = root.get("message");
                if (messageNode != null && !messageNode.isNull() && !messageNode.asText().isBlank()) {
                    Violation violation = new Violation("externalService", messageNode.asText());
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ValidationErrorResponse(Collections.singletonList(violation)));
                }

                // error
                JsonNode errorNode = root.get("error");
                if (errorNode != null && !errorNode.isNull() && !errorNode.asText().isBlank()) {
                    Violation violation = new Violation("externalService", errorNode.asText());
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ValidationErrorResponse(Collections.singletonList(violation)));
                }

                // Fallback: если не нашли violations, message, error — возвращаем информативную ошибку, а не exception
                Violation violation = new Violation("externalService", "Ошибка во внешней системе: неизвестный формат ответа");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ValidationErrorResponse(Collections.singletonList(violation)));

            } catch (Exception parseEx) {
                Violation violation = new Violation("externalService", "Ошибка во внешней системе: не удалось разобрать JSON-ответ: " + parseEx.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ValidationErrorResponse(Collections.singletonList(violation)));
            }
        }

        // Если совсем пусто — возвращаем стандартный fallback
        Violation violation = new Violation("externalService", "Ошибка во внешней системе");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ValidationErrorResponse(Collections.singletonList(violation)));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}





