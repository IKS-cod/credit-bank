package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateGenderBorrowerTest {

    private final ValidateGenderBorrower validator = new ValidateGenderBorrower();

    Gender gender = Gender.MALE;

    @Test
    @DisplayName("validateGender returns false for null")
    void testNullGender() {
        assertFalse(validator.validateGender(null));
    }

    @Test
    @DisplayName("validateGender returns true for non-null gender")
    void testNonNullGender() {
        for (Gender gender : Gender.values()) {
            assertTrue(validator.validateGender(gender));
        }
    }
}
