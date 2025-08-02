package com.neoflex.statement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        return birthdate.plusYears(18).isBefore(LocalDate.now()) || birthdate.plusYears(18).isEqual(LocalDate.now());
    }
}
