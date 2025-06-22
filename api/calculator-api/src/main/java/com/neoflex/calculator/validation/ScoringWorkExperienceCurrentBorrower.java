package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ScoringWorkExperienceCurrentBorrower {

    /**
     * Проверяет, что текущий трудовой стаж на последнем месте работы не менее 3 месяцев.
     *
     * @param workExperienceCurrent текущий трудовой стаж в месяцах
     * @return true, если стаж ≥ 3 месяцев, иначе false
     */
    public boolean scoringWorkExperienceCurrentBorrower(Integer workExperienceCurrent) {
        return workExperienceCurrent >= 3;
    }
}

