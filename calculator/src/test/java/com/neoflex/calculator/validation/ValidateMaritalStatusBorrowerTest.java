package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.MaritalStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateMaritalStatusBorrowerTest {

    private final ValidateMaritalStatusBorrower validator = new ValidateMaritalStatusBorrower();

    MaritalStatus status = MaritalStatus.MARRIED;

    @Test
    @DisplayName("validateMaritalStatusBorrower returns false for null")
    void testNullMaritalStatus() {
        assertFalse(validator.validateMaritalStatusBorrower(null));
    }

    @Test
    @DisplayName("validateMaritalStatusBorrower returns true for non-null status")
    void testNonNullMaritalStatus() {
        for (MaritalStatus status : MaritalStatus.values()) {
            assertTrue(validator.validateMaritalStatusBorrower(status));
        }
    }
}
