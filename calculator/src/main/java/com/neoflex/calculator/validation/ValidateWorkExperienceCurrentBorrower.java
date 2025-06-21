package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateWorkExperienceCurrentBorrower {

    /**
     * Проверяет, что значение не null и не отрицательное.
     */
    public boolean validateWorkExperienceCurrentBorrower(Integer workExperienceCurrent) {
        if (workExperienceCurrent == null) {
            return false;
        }
        return workExperienceCurrent >= 0;
    }
}

