package com.neoflex.calculator.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateMiddleNameBorrowerTest {

    private ValidateMiddleNameBorrower validator;

    @BeforeEach
    void setUp() {
        String namePattern = "^[a-zA-Z]{2,30}$";
        validator = new ValidateMiddleNameBorrower(namePattern);
    }

    @Test
    @DisplayName("validateMiddleNameBorrower returns true for null value")
    void testNullValue() {
        assertTrue(validator.validateMiddleNameBorrower(null));
    }

    @Test
    @DisplayName("validateMiddleNameBorrower returns true for empty string")
    void testEmptyValue() {
        assertTrue(validator.validateMiddleNameBorrower(""));
    }

    @Test
    @DisplayName("validateMiddleNameBorrower returns false for too short middle name")
    void testTooShortName() {
        assertFalse(validator.validateMiddleNameBorrower("A")); // 1 letter
    }

    @Test
    @DisplayName("validateMiddleNameBorrower returns false for too long middle name")
    void testTooLongName() {
        String longName = "A".repeat(31); // 31 letter
        assertFalse(validator.validateMiddleNameBorrower(longName));
    }

    @Test
    @DisplayName("validateMiddleNameBorrower returns false for invalid characters")
    void testInvalidCharacters() {
        assertFalse(validator.validateMiddleNameBorrower("Ivan1"));
        assertFalse(validator.validateMiddleNameBorrower("Иван")); // кириллица
        assertFalse(validator.validateMiddleNameBorrower("John_Doe"));
        assertFalse(validator.validateMiddleNameBorrower("Anna-Marie"));
    }

    @Test
    @DisplayName("validateMiddleNameBorrower returns true for valid middle names")
    void testValidNames() {
        assertTrue(validator.validateMiddleNameBorrower("Ivan"));
        assertTrue(validator.validateMiddleNameBorrower("Anna"));
        assertTrue(validator.validateMiddleNameBorrower("John"));
        assertTrue(validator.validateMiddleNameBorrower("Elizabeth"));
    }
}
