package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ValidateAmountCreditTest {

    private final ValidateAmountCredit validator = new ValidateAmountCredit();

    @Test
    @DisplayName("validateAmountCredit returns false for null amount")
    void testNullAmount() {
        assertFalse(validator.validateAmountCredit(null));
    }

    @Test
    @DisplayName("validateAmountCredit returns false for amount less than 20000")
    void testAmountLessThanMinimum() {
        assertFalse(validator.validateAmountCredit(BigDecimal.valueOf(19999.99)));
        assertFalse(validator.validateAmountCredit(BigDecimal.ZERO));
        assertFalse(validator.validateAmountCredit(BigDecimal.valueOf(-1000)));
    }

    @Test
    @DisplayName("validateAmountCredit returns true for amount equal to 20000")
    void testAmountEqualMinimum() {
        assertTrue(validator.validateAmountCredit(BigDecimal.valueOf(20000)));
    }

    @Test
    @DisplayName("validateAmountCredit returns true for amount greater than 20000")
    void testAmountGreaterThanMinimum() {
        assertTrue(validator.validateAmountCredit(BigDecimal.valueOf(20000.01)));
        assertTrue(validator.validateAmountCredit(BigDecimal.valueOf(1000000)));
    }
}
