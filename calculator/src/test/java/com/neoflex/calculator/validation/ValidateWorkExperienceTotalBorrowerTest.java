package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateWorkExperienceTotalBorrowerTest {

    private final ValidateWorkExperienceTotalBorrower validator = new ValidateWorkExperienceTotalBorrower();

    @Test
    @DisplayName("validateWorkExperienceTotalBorrower returns false for null")
    void testNullValue() {
        assertFalse(validator.validateWorkExperienceTotalBorrower(null));
    }

    @Test
    @DisplayName("validateWorkExperienceTotalBorrower returns false for negative values")
    void testNegativeValues() {
        assertFalse(validator.validateWorkExperienceTotalBorrower(-1));
        assertFalse(validator.validateWorkExperienceTotalBorrower(-100));
    }

    @Test
    @DisplayName("validateWorkExperienceTotalBorrower returns true for zero and positive values")
    void testZeroAndPositiveValues() {
        assertTrue(validator.validateWorkExperienceTotalBorrower(0));
        assertTrue(validator.validateWorkExperienceTotalBorrower(1));
        assertTrue(validator.validateWorkExperienceTotalBorrower(10));
        assertTrue(validator.validateWorkExperienceTotalBorrower(100));
    }
}
