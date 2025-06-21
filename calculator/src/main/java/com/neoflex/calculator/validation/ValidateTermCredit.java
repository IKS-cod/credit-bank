package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateTermCredit {
    public boolean validateTermCredit(Integer term) {
        if (term == null || term < 6) {
            return false;
        }
        return true;
    }
}
