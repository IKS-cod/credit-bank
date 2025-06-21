package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatePositionBorrowerTest {

    private final ValidatePositionBorrower validator = new ValidatePositionBorrower();

    Position position = Position.MIDDLE_MANAGER;

    @Test
    @DisplayName("validatePositionBorrower returns false for null")
    void testNullPosition() {
        assertFalse(validator.validatePositionBorrower(null));
    }

    @Test
    @DisplayName("validatePositionBorrower returns true for non-null position")
    void testNonNullPosition() {
        for (Position position : Position.values()) {
            assertTrue(validator.validatePositionBorrower(position));
        }
    }
}
