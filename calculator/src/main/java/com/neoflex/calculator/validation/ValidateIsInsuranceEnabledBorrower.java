package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateIsInsuranceEnabledBorrower {

    /**
     * Проверяет, что поле не null.
     *
     * @param isInsuranceEnabled значение поля
     * @return true, если не null, иначе false
     */
    public boolean validateIsInsuranceEnabledBorrower(Boolean isInsuranceEnabled) {
        return isInsuranceEnabled != null;
    }
}
