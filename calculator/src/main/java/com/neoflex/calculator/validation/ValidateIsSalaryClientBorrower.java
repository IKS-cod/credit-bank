package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateIsSalaryClientBorrower {

    /**
     * Проверяет, что поле не null.
     */
    public boolean validateIsSalaryClientBorrower(Boolean isSalaryClient) {
        return isSalaryClient != null;
    }
}

