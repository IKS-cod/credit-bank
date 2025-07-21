package com.neoflex.calculator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeBetweenValidator implements ConstraintValidator<AgeBetween, LocalDate> {

    private int minAge;
    private int maxAge;

    @Override
    public void initialize(AgeBetween constraintAnnotation) {
        this.minAge = constraintAnnotation.min();
        this.maxAge = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        return age >= minAge && age <= maxAge;
    }
}

