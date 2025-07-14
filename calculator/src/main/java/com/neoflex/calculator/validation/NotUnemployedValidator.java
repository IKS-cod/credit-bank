package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.EmploymentStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotUnemployedValidator implements ConstraintValidator<NotUnemployed, EmploymentStatus> {
    @Override
    public boolean isValid(EmploymentStatus value, ConstraintValidatorContext context) {
        return !EmploymentStatus.UNEMPLOYED.equals(value);
    }
}

