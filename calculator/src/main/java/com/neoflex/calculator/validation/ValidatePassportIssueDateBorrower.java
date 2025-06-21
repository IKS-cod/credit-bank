package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidatePassportIssueDateBorrower {

    /**
     * Проверяет, что дата не null и не в будущем.
     */
    public boolean validatePassportIssueDateBorrower(LocalDate passportIssueDate) {
        if (passportIssueDate == null) {
            return false;
        }
        return !passportIssueDate.isAfter(LocalDate.now());
    }
}

