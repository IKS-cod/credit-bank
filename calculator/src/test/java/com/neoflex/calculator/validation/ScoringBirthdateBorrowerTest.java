package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoringBirthdateBorrowerTest {

    private final ScoringBirthdateBorrower scorer = new ScoringBirthdateBorrower();

    @Test
    @DisplayName("scoringBirthdateBorrower returns false for age less than 20")
    void testAgeLessThan20() {
        LocalDate birthdate = LocalDate.now().minusYears(19).plusDays(1); // чуть меньше 20 лет
        assertFalse(scorer.scoringBirthdateBorrower(birthdate));
    }

    @Test
    @DisplayName("scoringBirthdateBorrower returns true for age exactly 20")
    void testAgeExactly20() {
        LocalDate birthdate = LocalDate.now().minusYears(20);
        assertTrue(scorer.scoringBirthdateBorrower(birthdate));
    }

    @Test
    @DisplayName("scoringBirthdateBorrower returns true for age between 20 and 65")
    void testAgeBetween20And65() {
        LocalDate birthdate = LocalDate.now().minusYears(40);
        assertTrue(scorer.scoringBirthdateBorrower(birthdate));
    }

    @Test
    @DisplayName("scoringBirthdateBorrower returns true for age exactly 65")
    void testAgeExactly65() {
        LocalDate birthdate = LocalDate.now().minusYears(65);
        assertTrue(scorer.scoringBirthdateBorrower(birthdate));
    }

    @Test
    @DisplayName("scoringBirthdateBorrower returns false for age greater than 65")
    void testAgeGreaterThan65() {
        LocalDate birthdate = LocalDate.now().minusYears(66); // ровно 66 лет назад
        assertFalse(scorer.scoringBirthdateBorrower(birthdate));
    }
}
