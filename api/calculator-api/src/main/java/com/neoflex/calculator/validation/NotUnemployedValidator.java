package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.exception.UnemployedApplicantException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotUnemployedValidator implements ConstraintValidator<NotUnemployed, EmploymentStatus> {
    @Override
    public boolean isValid(EmploymentStatus value, ConstraintValidatorContext context) {
        if (EmploymentStatus.UNEMPLOYED.equals(value)) {
            throw new UnemployedApplicantException("Отказ в кредите: Кредит не предоставляется безработным");
        }
        return true;
    }
}

