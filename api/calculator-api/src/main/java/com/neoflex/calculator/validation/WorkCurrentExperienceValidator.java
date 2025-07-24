package com.neoflex.calculator.validation;

import com.neoflex.calculator.exception.WorkCurrentExperienceException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WorkCurrentExperienceValidator implements ConstraintValidator<ValidWorkCurrentExperience, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidWorkCurrentExperience annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value < min) {
            throw new WorkCurrentExperienceException("Текущий трудовой стаж менее 3 месяцев — отказ в выдаче кредита");
        }
        if (value > max) {
            throw new WorkCurrentExperienceException("Текущий трудовой стаж не может превышать 600 месяцев");
        }
        return true;
    }
}

