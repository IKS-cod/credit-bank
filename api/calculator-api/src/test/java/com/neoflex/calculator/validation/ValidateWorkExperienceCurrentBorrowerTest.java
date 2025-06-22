package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateWorkExperienceCurrentBorrowerTest {

    private final ValidateWorkExperienceCurrentBorrower validator = new ValidateWorkExperienceCurrentBorrower();

    @Test
    @DisplayName("validateWorkExperienceCurrentBorrower returns false for null")
    void testNullValue() {
        assertFalse(validator.validateWorkExperienceCurrentBorrower(null));
    }

    @Test
    @DisplayName("validateWorkExperienceCurrentBorrower returns false for negative values")
    void testNegativeValues() {
        assertFalse(validator.validateWorkExperienceCurrentBorrower(-1));
        assertFalse(validator.validateWorkExperienceCurrentBorrower(-100));
    }

    @Test
    @DisplayName("validateWorkExperienceCurrentBorrower returns true for zero and positive values")
    void testZeroAndPositiveValues() {
        assertTrue(validator.validateWorkExperienceCurrentBorrower(0));
        assertTrue(validator.validateWorkExperienceCurrentBorrower(1));
        assertTrue(validator.validateWorkExperienceCurrentBorrower(10));
        assertTrue(validator.validateWorkExperienceCurrentBorrower(100));
    }
}
