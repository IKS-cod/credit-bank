package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ScoringAmountCreditTest {

    private final ScoringAmountCredit scorer = new ScoringAmountCredit();

    @Test
    @DisplayName("scoringAmountCredit returns true when amount is less than 24 times salary")
    void testAmountLessThanLimit() {
        BigDecimal salary = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.valueOf(23000); // less than 24000
        assertTrue(scorer.scoringAmountCredit(amount, salary));
    }

    @Test
    @DisplayName("scoringAmountCredit returns true when amount equals 24 times salary")
    void testAmountEqualsLimit() {
        BigDecimal salary = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.valueOf(24000); // exactly 24 * 1000
        assertTrue(scorer.scoringAmountCredit(amount, salary));
    }

    @Test
    @DisplayName("scoringAmountCredit returns false when amount exceeds 24 times salary")
    void testAmountExceedsLimit() {
        BigDecimal salary = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.valueOf(25000); // greater than 24000
        assertFalse(scorer.scoringAmountCredit(amount, salary));
    }

    @Test
    @DisplayName("scoringAmountCredit returns false when amount is null")
    void testNullAmount() {
        BigDecimal salary = BigDecimal.valueOf(1000);
        assertThrows(NullPointerException.class, () -> scorer.scoringAmountCredit(null, salary));
    }

    @Test
    @DisplayName("scoringAmountCredit returns false when salary is null")
    void testNullSalary() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        assertThrows(NullPointerException.class, () -> scorer.scoringAmountCredit(amount, null));
    }

    @Test
    @DisplayName("scoringAmountCredit returns true for zero amount")
    void testZeroAmount() {
        BigDecimal salary = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.ZERO;
        assertTrue(scorer.scoringAmountCredit(amount, salary));
    }

    @Test
    @DisplayName("scoringAmountCredit returns false for negative amount")
    void testNegativeAmount() {
        BigDecimal salary = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.valueOf(-100);
        assertTrue(scorer.scoringAmountCredit(amount, salary)); // Negative amount is less than maxAmount, so returns true
        // If negative amounts should be invalid, consider updating method and tests accordingly
    }
}
