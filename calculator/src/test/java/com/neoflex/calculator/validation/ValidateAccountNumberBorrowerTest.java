package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateAccountNumberBorrowerTest {
    private final ValidateAccountNumberBorrower validator = new ValidateAccountNumberBorrower();

    @Test
    @DisplayName("validateAccountNumberBorrower returns false for null")
    void testNullAccountNumber() {
        assertFalse(validator.validateAccountNumberBorrower(null));
    }

    @Test
    @DisplayName("validateAccountNumberBorrower returns false for blank string")
    void testBlankAccountNumber() {
        assertFalse(validator.validateAccountNumberBorrower(""));
        assertFalse(validator.validateAccountNumberBorrower("    "));
    }

    @Test
    @DisplayName("validateAccountNumberBorrower returns false for string with less than 20 digits")
    void testLessThan20Digits() {
        assertFalse(validator.validateAccountNumberBorrower("1234567890123456789")); // 19 digits
    }

    @Test
    @DisplayName("validateAccountNumberBorrower returns false for string with more than 20 digits")
    void testMoreThan20Digits() {
        assertFalse(validator.validateAccountNumberBorrower("123456789012345678901")); // 21 digits
    }

    @Test
    @DisplayName("validateAccountNumberBorrower returns false for string with non-digit characters")
    void testNonDigitCharacters() {
        assertFalse(validator.validateAccountNumberBorrower("1234567890123456789A")); // last char letter
        assertFalse(validator.validateAccountNumberBorrower("1234-5678-9012-3456-7890")); // dashes
        assertFalse(validator.validateAccountNumberBorrower("1234567890abcdefghij")); // letters
    }

    @Test
    @DisplayName("validateAccountNumberBorrower returns true for valid 20-digit number")
    void testValidAccountNumber() {
        assertTrue(validator.validateAccountNumberBorrower("12345678901234567890"));
    }
}