package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateIsInsuranceEnabledBorrowerTest {

    private final ValidateIsInsuranceEnabledBorrower validator = new ValidateIsInsuranceEnabledBorrower();

    @Test
    @DisplayName("validateIsInsuranceEnabledBorrower returns false for null")
    void testNullValue() {
        assertFalse(validator.validateIsInsuranceEnabledBorrower(null));
    }

    @Test
    @DisplayName("validateIsInsuranceEnabledBorrower returns true for true")
    void testTrueValue() {
        assertTrue(validator.validateIsInsuranceEnabledBorrower(Boolean.TRUE));
    }

    @Test
    @DisplayName("validateIsInsuranceEnabledBorrower returns true for false")
    void testFalseValue() {
        assertTrue(validator.validateIsInsuranceEnabledBorrower(Boolean.FALSE));
    }
}
