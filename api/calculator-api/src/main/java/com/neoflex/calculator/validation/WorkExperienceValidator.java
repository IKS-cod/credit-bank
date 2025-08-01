package com.neoflex.calculator.validation;

import com.neoflex.calculator.exception.WorkExperienceException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WorkExperienceValidator implements ConstraintValidator<ValidWorkExperience, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidWorkExperience constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        if (value < min) {
            throw new WorkExperienceException("Общий трудовой стаж менее 18 месяцев — отказ в выдаче кредита");
        }
        if (value > max) {
            throw new WorkExperienceException("Общий трудовой стаж не может превышать 600 месяцев");
        }
        return true;
    }
}

