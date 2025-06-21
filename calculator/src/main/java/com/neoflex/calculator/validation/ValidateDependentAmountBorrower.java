package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateDependentAmountBorrower {

    /**
     * Проверяет, что значение не null и находится в диапазоне от 0.
     */
    public boolean validateDependentAmountBorrower(Integer dependentAmount) {
        if (dependentAmount == null) {
            return false;
        }
        return dependentAmount >= 0;
    }
}
