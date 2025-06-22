package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ValidateSalaryBorrowerTest {

    private final ValidateSalaryBorrower validator = new ValidateSalaryBorrower();

    @Test
    @DisplayName("validateSalaryBorrower returns false for null salary")
    void testNullSalary() {
        assertFalse(validator.validateSalaryBorrower(null));
    }

    @Test
    @DisplayName("validateSalaryBorrower returns false for zero salary")
    void testZeroSalary() {
        assertFalse(validator.validateSalaryBorrower(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("validateSalaryBorrower returns false for negative salary")
    void testNegativeSalary() {
        assertFalse(validator.validateSalaryBorrower(BigDecimal.valueOf(-1000)));
        assertFalse(validator.validateSalaryBorrower(BigDecimal.valueOf(-0.01)));
    }

    @Test
    @DisplayName("validateSalaryBorrower returns true for positive salary")
    void testPositiveSalary() {
        assertTrue(validator.validateSalaryBorrower(BigDecimal.valueOf(0.01)));
        assertTrue(validator.validateSalaryBorrower(BigDecimal.valueOf(1000)));
        assertTrue(validator.validateSalaryBorrower(new BigDecimal("1000000.00")));
    }
}
