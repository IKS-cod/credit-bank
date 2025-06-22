package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidateBirthdateBorrowerTest {

    private final ValidateBirthdateBorrower validator = new ValidateBirthdateBorrower();

    @Test
    @DisplayName("validateBirthdateBorrower returns false for null birthdate")
    void testNullBirthdate() {
        assertFalse(validator.validateBirthdateBorrower(null));
    }

    @Test
    @DisplayName("validateBirthdateBorrower returns false for birthdate in the future")
    void testBirthdateInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertFalse(validator.validateBirthdateBorrower(futureDate));
    }

    @Test
    @DisplayName("validateBirthdateBorrower returns false for age less than 18")
    void testAgeLessThan18() {
        LocalDate lessThan18 = LocalDate.now().minusYears(17).plusDays(1);
        assertFalse(validator.validateBirthdateBorrower(lessThan18));
    }

    @Test
    @DisplayName("validateBirthdateBorrower returns true for age exactly 18")
    void testAgeExactly18() {
        LocalDate exactly18 = LocalDate.now().minusYears(18);
        assertTrue(validator.validateBirthdateBorrower(exactly18));
    }

    @Test
    @DisplayName("validateBirthdateBorrower returns true for age greater than 18")
    void testAgeGreaterThan18() {
        LocalDate greaterThan18 = LocalDate.now().minusYears(25);
        assertTrue(validator.validateBirthdateBorrower(greaterThan18));
    }
}
