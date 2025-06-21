package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.EmploymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateEmploymentStatusBorrowerTest {

    private final ValidateEmploymentStatusBorrower validator = new ValidateEmploymentStatusBorrower();

    EmploymentStatus status = EmploymentStatus.SELF_EMPLOYED;

    @Test
    @DisplayName("validateEmploymentStatusBorrower returns false for null")
    void testNullEmploymentStatus() {
        assertFalse(validator.validateEmploymentStatusBorrower(null));
    }

    @Test
    @DisplayName("validateEmploymentStatusBorrower returns true for non-null status")
    void testNonNullEmploymentStatus() {
        for (EmploymentStatus status : EmploymentStatus.values()) {
            assertTrue(validator.validateEmploymentStatusBorrower(status));
        }
    }
}
