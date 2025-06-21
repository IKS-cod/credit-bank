package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateSalaryBorrower {

    /**
     * Проверяет, что зарплата не null и положительна.
     */
    public boolean validateSalaryBorrower(BigDecimal salary) {
        if (salary == null) {
            return false;
        }
        return salary.compareTo(BigDecimal.ZERO) > 0;
    }
}

