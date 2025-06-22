package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatePassportIssueBranchBorrowerTest {

    private final ValidatePassportIssueBranchBorrower validator = new ValidatePassportIssueBranchBorrower();

    @Test
    @DisplayName("validatePassportIssueBranchBorrower returns false for null")
    void testNullValue() {
        assertFalse(validator.validatePassportIssueBranchBorrower(null));
    }

    @Test
    @DisplayName("validatePassportIssueBranchBorrower returns false for empty string")
    void testEmptyString() {
        assertFalse(validator.validatePassportIssueBranchBorrower(""));
    }

    @Test
    @DisplayName("validatePassportIssueBranchBorrower returns false for string with only spaces")
    void testOnlySpaces() {
        assertFalse(validator.validatePassportIssueBranchBorrower("   "));
        assertFalse(validator.validatePassportIssueBranchBorrower("\t\n"));
    }

    @Test
    @DisplayName("validatePassportIssueBranchBorrower returns true for non-empty string")
    void testValidString() {
        assertTrue(validator.validatePassportIssueBranchBorrower("Passport Office #123"));
        assertTrue(validator.validatePassportIssueBranchBorrower("  Some Branch  "));
    }
}
