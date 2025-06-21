package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateAmountCredit {
    public boolean validateAmountCredit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.valueOf(20000)) < 0) {
            return false;
        }
        return true;
    }
}
