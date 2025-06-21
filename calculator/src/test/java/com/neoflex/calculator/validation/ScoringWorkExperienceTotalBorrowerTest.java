package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoringWorkExperienceTotalBorrowerTest {

    private final ScoringWorkExperienceTotalBorrower scorer = new ScoringWorkExperienceTotalBorrower();

    @Test
    @DisplayName("scoringWorkExperienceTotalBorrower returns false for values less than 18")
    void testLessThanEighteen() {
        assertFalse(scorer.scoringWorkExperienceTotalBorrower(0));
        assertFalse(scorer.scoringWorkExperienceTotalBorrower(1));
        assertFalse(scorer.scoringWorkExperienceTotalBorrower(17));
        assertFalse(scorer.scoringWorkExperienceTotalBorrower(-5));
    }

    @Test
    @DisplayName("scoringWorkExperienceTotalBorrower returns true for values equal or greater than 18")
    void testEighteenOrMore() {
        assertTrue(scorer.scoringWorkExperienceTotalBorrower(18));
        assertTrue(scorer.scoringWorkExperienceTotalBorrower(24));
        assertTrue(scorer.scoringWorkExperienceTotalBorrower(36));
    }
}
