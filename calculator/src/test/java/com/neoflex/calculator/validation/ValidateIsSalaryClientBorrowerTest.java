package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateIsSalaryClientBorrowerTest {

    private final ValidateIsSalaryClientBorrower validator = new ValidateIsSalaryClientBorrower();

    @Test
    @DisplayName("validateIsSalaryClientBorrower returns false for null")
    void testNullValue() {
        assertFalse(validator.validateIsSalaryClientBorrower(null));
    }

    @Test
    @DisplayName("validateIsSalaryClientBorrower returns true for true")
    void testTrueValue() {
        assertTrue(validator.validateIsSalaryClientBorrower(Boolean.TRUE));
    }

    @Test
    @DisplayName("validateIsSalaryClientBorrower returns true for false")
    void testFalseValue() {
        assertTrue(validator.validateIsSalaryClientBorrower(Boolean.FALSE));
    }
}
