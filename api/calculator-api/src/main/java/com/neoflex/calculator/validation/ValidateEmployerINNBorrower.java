package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateEmployerINNBorrower {
    /**
     * Проверяет, что ИНН обязательно заполнен и состоит из 10 или 12 цифр.
     */
    public boolean validateEmployerINNBorrower(String employerINN) {
        if (employerINN == null || employerINN.isBlank()) {
            return false;
        }
        return employerINN.matches("\\d{10}|\\d{12}");
    }
}

