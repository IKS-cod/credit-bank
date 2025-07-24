package com.neoflex.calculator.validation;

import com.neoflex.calculator.exception.AgeRestrictionException;
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
        if (age < minAge || age > maxAge) {
            throw new AgeRestrictionException(
                    String.format("Отказ в кредите: Возраст должен быть в диапазоне от %d до %d лет", minAge, maxAge)
            );
        }
        return true;
    }
}

