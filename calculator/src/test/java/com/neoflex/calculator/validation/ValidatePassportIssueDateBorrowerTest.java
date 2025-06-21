package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidatePassportIssueDateBorrowerTest {

    private final ValidatePassportIssueDateBorrower validator = new ValidatePassportIssueDateBorrower();

    @Test
    @DisplayName("validatePassportIssueDateBorrower returns false for null date")
    void testNullDate() {
        assertFalse(validator.validatePassportIssueDateBorrower(null));
    }

    @Test
    @DisplayName("validatePassportIssueDateBorrower returns false for future date")
    void testFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertFalse(validator.validatePassportIssueDateBorrower(futureDate));
    }

    @Test
    @DisplayName("validatePassportIssueDateBorrower returns true for today")
    void testTodayDate() {
        LocalDate today = LocalDate.now();
        assertTrue(validator.validatePassportIssueDateBorrower(today));
    }

    @Test
    @DisplayName("validatePassportIssueDateBorrower returns true for past date")
    void testPastDate() {
        LocalDate pastDate = LocalDate.now().minusYears(5);
        assertTrue(validator.validatePassportIssueDateBorrower(pastDate));
    }
}
