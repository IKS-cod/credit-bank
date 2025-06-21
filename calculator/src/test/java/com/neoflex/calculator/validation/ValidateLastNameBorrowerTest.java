package com.neoflex.calculator.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateLastNameBorrowerTest {

    private ValidateLastNameBorrower validator;

    @BeforeEach
    void setUp() {
        String namePattern = "^[a-zA-Z]{2,30}$";
        validator = new ValidateLastNameBorrower(namePattern);
    }

    @Test
    @DisplayName("validateLastNameBorrower returns false for null value")
    void testNullValue() {
        assertFalse(validator.validateLastNameBorrower(null));
    }

    @Test
    @DisplayName("validateLastNameBorrower returns false for empty string")
    void testEmptyValue() {
        assertFalse(validator.validateLastNameBorrower(""));
    }

    @Test
    @DisplayName("validateLastNameBorrower returns false for too short name")
    void testTooShortName() {
        assertFalse(validator.validateLastNameBorrower("A")); // 1 letter
    }

    @Test
    @DisplayName("validateLastNameBorrower returns false for too long name")
    void testTooLongName() {
        String longName = "A".repeat(31); // 31 letter
        assertFalse(validator.validateLastNameBorrower(longName));
    }

    @Test
    @DisplayName("validateLastNameBorrower returns false for invalid characters")
    void testInvalidCharacters() {
        assertFalse(validator.validateLastNameBorrower("Ivan1"));
        assertFalse(validator.validateLastNameBorrower("Иван")); // кириллица
        assertFalse(validator.validateLastNameBorrower("John_Doe"));
        assertFalse(validator.validateLastNameBorrower("Anna-Marie"));
    }

    @Test
    @DisplayName("validateLastNameBorrower returns true for valid names")
    void testValidNames() {
        assertTrue(validator.validateLastNameBorrower("Ivan"));
        assertTrue(validator.validateLastNameBorrower("Anna"));
        assertTrue(validator.validateLastNameBorrower("John"));
        assertTrue(validator.validateLastNameBorrower("Elizabeth"));
    }
}
