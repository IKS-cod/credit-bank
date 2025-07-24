package com.neoflex.calculator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoanAmountWithinSalaryLimitValidator.class)
public @interface LoanAmountWithinSalaryLimit {
    String message() default "Превышен лимит суммы кредита (не более 24 зарплат)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

