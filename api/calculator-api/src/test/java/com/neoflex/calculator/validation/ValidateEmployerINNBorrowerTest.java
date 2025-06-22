package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateEmployerINNBorrowerTest {

    private final ValidateEmployerINNBorrower validator = new ValidateEmployerINNBorrower();

    @Test
    @DisplayName("validateEmployerINNBorrower returns false for null")
    void testNullINN() {
        assertFalse(validator.validateEmployerINNBorrower(null));
    }

    @Test
    @DisplayName("validateEmployerINNBorrower returns false for blank string")
    void testBlankINN() {
        assertFalse(validator.validateEmployerINNBorrower(""));
        assertFalse(validator.validateEmployerINNBorrower("   "));
    }

    @Test
    @DisplayName("validateEmployerINNBorrower returns false for INN with invalid length")
    void testInvalidLengthINN() {
        assertFalse(validator.validateEmployerINNBorrower("123456789"));    // 9 digits
        assertFalse(validator.validateEmployerINNBorrower("12345678901"));  // 11 digits
        assertFalse(validator.validateEmployerINNBorrower("1234567890123")); // 13 digits
    }

    @Test
    @DisplayName("validateEmployerINNBorrower returns false for INN with non-digit characters")
    void testNonDigitINN() {
        assertFalse(validator.validateEmployerINNBorrower("123456789A"));   // 9 digits + 1 letter
        assertFalse(validator.validateEmployerINNBorrower("12345!7890"));   // special char
        assertFalse(validator.validateEmployerINNBorrower("12345 67890"));  // space
    }

    @Test
    @DisplayName("validateEmployerINNBorrower returns true for valid 10-digit INN")
    void testValid10DigitINN() {
        assertTrue(validator.validateEmployerINNBorrower("1234567890"));
    }

    @Test
    @DisplayName("validateEmployerINNBorrower returns true for valid 12-digit INN")
    void testValid12DigitINN() {
        assertTrue(validator.validateEmployerINNBorrower("123456789012"));
    }
}
