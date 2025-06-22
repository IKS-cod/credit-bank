package com.neoflex.calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoringWorkExperienceCurrentBorrowerTest {

    private final ScoringWorkExperienceCurrentBorrower scorer = new ScoringWorkExperienceCurrentBorrower();

    @Test
    @DisplayName("scoringWorkExperienceCurrentBorrower returns false for values less than 3")
    void testLessThanThree() {
        assertFalse(scorer.scoringWorkExperienceCurrentBorrower(0));
        assertFalse(scorer.scoringWorkExperienceCurrentBorrower(1));
        assertFalse(scorer.scoringWorkExperienceCurrentBorrower(2));
        assertFalse(scorer.scoringWorkExperienceCurrentBorrower(-1));
    }

    @Test
    @DisplayName("scoringWorkExperienceCurrentBorrower returns true for values equal or greater than 3")
    void testThreeOrMore() {
        assertTrue(scorer.scoringWorkExperienceCurrentBorrower(3));
        assertTrue(scorer.scoringWorkExperienceCurrentBorrower(6));
        assertTrue(scorer.scoringWorkExperienceCurrentBorrower(12));
    }
}
