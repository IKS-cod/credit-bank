package com.neoflex.calculator.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatePassportSeriesTest {

    private ValidatePassportSeries validator;

    @BeforeEach
    void setUp() {
        String passportSeriesPattern = "^\\d{4}$";
        validator = new ValidatePassportSeries(passportSeriesPattern);
    }

    @Test
    @DisplayName("validatePassportSeries returns false for null")
    void testNullSeries() {
        assertFalse(validator.validatePassportSeries(null));
    }

    @Test
    @DisplayName("validatePassportSeries returns false for empty string")
    void testEmptySeries() {
        assertFalse(validator.validatePassportSeries(""));
    }

    @Test
    @DisplayName("validatePassportSeries returns false for invalid format")
    void testInvalidFormat() {
        assertFalse(validator.validatePassportSeries("123"));      // слишком короткая
        assertFalse(validator.validatePassportSeries("12345"));    // слишком длинная
        assertFalse(validator.validatePassportSeries("12A4"));     // буква внутри
        assertFalse(validator.validatePassportSeries("12 4"));     // пробел
        assertFalse(validator.validatePassportSeries("!@#$"));     // спецсимволы
    }

    @Test
    @DisplayName("validatePassportSeries returns true for valid format")
    void testValidFormat() {
        assertTrue(validator.validatePassportSeries("1234"));
        assertTrue(validator.validatePassportSeries("0000"));
        assertTrue(validator.validatePassportSeries("9999"));
    }
}
