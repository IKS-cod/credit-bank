package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateWorkExperienceTotalBorrower {

    /**
     * Проверяет, что значение не null и не отрицательное.
     */
    public boolean validateWorkExperienceTotalBorrower(Integer workExperienceTotal) {
        if (workExperienceTotal == null) {
            return false;
        }
        return workExperienceTotal >= 0;
    }
}

