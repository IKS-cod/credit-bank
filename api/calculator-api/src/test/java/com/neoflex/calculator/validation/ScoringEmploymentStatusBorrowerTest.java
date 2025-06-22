package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.EmploymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoringEmploymentStatusBorrowerTest {

    private final ScoringEmploymentStatusBorrower scorer = new ScoringEmploymentStatusBorrower();

    EmploymentStatus status = EmploymentStatus.SELF_EMPLOYED;

    @Test
    @DisplayName("scoringEmploymentStatusBorrower returns false for UNEMPLOYED status")
    void testUnemployedStatus() {
        assertFalse(scorer.scoringEmploymentStatusBorrower(EmploymentStatus.UNEMPLOYED));
    }

    @Test
    @DisplayName("scoringEmploymentStatusBorrower returns true for other statuses")
    void testOtherStatuses() {
        for (EmploymentStatus status : EmploymentStatus.values()) {
            if (status != EmploymentStatus.UNEMPLOYED) {
                assertTrue(scorer.scoringEmploymentStatusBorrower(status));
            }
        }
    }

    @Test
    @DisplayName("scoringEmploymentStatusBorrower throws NullPointerException for null input")
    void testNullEmploymentStatus() {
        assertThrows(NullPointerException.class, () -> scorer.scoringEmploymentStatusBorrower(null));
    }
}
