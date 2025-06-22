package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateTermCreditTest {

    private final ValidateTermCredit validator = new ValidateTermCredit();

    @Test
    @DisplayName("validateTermCredit returns false for null term")
    void testNullTerm() {
        assertFalse(validator.validateTermCredit(null));
    }

    @Test
    @DisplayName("validateTermCredit returns false for term less than 6")
    void testTermLessThanSix() {
        assertFalse(validator.validateTermCredit(0));
        assertFalse(validator.validateTermCredit(1));
        assertFalse(validator.validateTermCredit(5));
    }

    @Test
    @DisplayName("validateTermCredit returns true for term equal to or greater than 6")
    void testTermSixOrMore() {
        assertTrue(validator.validateTermCredit(6));
        assertTrue(validator.validateTermCredit(12));
        assertTrue(validator.validateTermCredit(100));
    }
}
