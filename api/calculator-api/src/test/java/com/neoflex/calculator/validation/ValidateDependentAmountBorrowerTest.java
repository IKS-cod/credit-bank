package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateDependentAmountBorrowerTest {

    private final ValidateDependentAmountBorrower validator = new ValidateDependentAmountBorrower();

    @Test
    @DisplayName("validateDependentAmountBorrower returns false for null")
    void testNullDependentAmount() {
        assertFalse(validator.validateDependentAmountBorrower(null));
    }

    @Test
    @DisplayName("validateDependentAmountBorrower returns false for negative values")
    void testNegativeDependentAmount() {
        assertFalse(validator.validateDependentAmountBorrower(-1));
        assertFalse(validator.validateDependentAmountBorrower(-100));
    }

    @Test
    @DisplayName("validateDependentAmountBorrower returns true for zero")
    void testZeroDependentAmount() {
        assertTrue(validator.validateDependentAmountBorrower(0));
    }

    @Test
    @DisplayName("validateDependentAmountBorrower returns true for positive values")
    void testPositiveDependentAmount() {
        assertTrue(validator.validateDependentAmountBorrower(1));
        assertTrue(validator.validateDependentAmountBorrower(10));
        assertTrue(validator.validateDependentAmountBorrower(100));
    }
}
