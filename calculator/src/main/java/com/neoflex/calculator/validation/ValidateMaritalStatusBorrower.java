package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.MaritalStatus;
import org.springframework.stereotype.Component;

@Component
public class ValidateMaritalStatusBorrower {

    /**
     * Проверяет, что maritalStatus не null и соответствует enum MaritalStatus.
     */
    public boolean validateMaritalStatusBorrower(MaritalStatus maritalStatus) {
        return maritalStatus != null;
    }
}
