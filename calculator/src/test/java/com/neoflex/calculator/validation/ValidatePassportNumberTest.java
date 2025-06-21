package com.neoflex.calculator.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatePassportNumberTest {

    private ValidatePassportNumber validator;

    @BeforeEach
    void setUp() {
        String passportPattern = "^\\d{6}$";
        validator = new ValidatePassportNumber(passportPattern);
    }

    @Test
    @DisplayName("validatePassportNumber returns false for null")
    void testNullNumber() {
        assertFalse(validator.validatePassportNumber(null));
    }

    @Test
    @DisplayName("validatePassportNumber returns false for empty string")
    void testEmptyNumber() {
        assertFalse(validator.validatePassportNumber(""));
    }

    @Test
    @DisplayName("validatePassportNumber returns false for invalid format")
    void testInvalidFormat() {
        assertFalse(validator.validatePassportNumber("123"));          // слишком короткий
        assertFalse(validator.validatePassportNumber("1234567890"));   // слишком длинный
        assertFalse(validator.validatePassportNumber("ABC 123456"));   // пробел
        assertFalse(validator.validatePassportNumber("!@#$%^&*()"));   // спецсимволы
    }

    @Test
    @DisplayName("validatePassportNumber returns true for valid format")
    void testValidFormat() {
        assertTrue(validator.validatePassportNumber("123456"));
    }
}
