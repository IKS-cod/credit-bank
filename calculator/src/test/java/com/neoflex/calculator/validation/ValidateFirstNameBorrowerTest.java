package com.neoflex.calculator.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidateFirstNameBorrowerTest {

    private ValidateFirstNameBorrower validator;

    @BeforeEach
    void setUp() {
        String namePattern = "^[a-zA-Z]{2,30}$";
        validator = new ValidateFirstNameBorrower(namePattern);
    }

    @Test
    @DisplayName("validateFirstNameBorrower returns false for null value")
    void testNullValue() {
        assertFalse(validator.validateFirstNameBorrower(null));
    }

    @Test
    @DisplayName("validateFirstNameBorrower returns false for empty string")
    void testEmptyValue() {
        assertFalse(validator.validateFirstNameBorrower(""));
    }

    @Test
    @DisplayName("validateFirstNameBorrower returns false for too short name")
    void testTooShortName() {
        assertFalse(validator.validateFirstNameBorrower("A")); // 1 letter
    }

    @Test
    @DisplayName("validateFirstNameBorrower returns false for too long name")
    void testTooLongName() {
        String longName = "A".repeat(31); // 31 letter
        assertFalse(validator.validateFirstNameBorrower(longName));
    }

    @Test
    @DisplayName("validateFirstNameBorrower returns false for invalid characters")
    void testInvalidCharacters() {
        assertFalse(validator.validateFirstNameBorrower("Ivan1"));
        assertFalse(validator.validateFirstNameBorrower("Иван")); // кириллица
        assertFalse(validator.validateFirstNameBorrower("John_Doe"));
        assertFalse(validator.validateFirstNameBorrower("Anna-Marie"));
    }

    @Test
    @DisplayName("validateFirstNameBorrower returns true for valid names")
    void testValidNames() {
        assertTrue(validator.validateFirstNameBorrower("Ivan"));
        assertTrue(validator.validateFirstNameBorrower("Anna"));
        assertTrue(validator.validateFirstNameBorrower("John"));
        assertTrue(validator.validateFirstNameBorrower("Elizabeth"));
    }
}
