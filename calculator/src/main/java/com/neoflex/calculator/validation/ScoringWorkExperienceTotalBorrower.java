package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ScoringWorkExperienceTotalBorrower {

    /**
     * Проверяет, что общий трудовой стаж не менее 18 месяцев.
     *
     * @param workExperienceTotal общий трудовой стаж в месяцах
     * @return true, если стаж ≥ 18 месяцев, иначе false
     */
    public boolean scoringWorkExperienceTotalBorrower(Integer workExperienceTotal) {
        return workExperienceTotal >= 18;
    }
}
