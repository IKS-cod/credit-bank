package com.neoflex.calculator.validation;

import com.neoflex.calculator.dto.ScoringDataDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class LoanAmountWithinSalaryLimitValidator implements ConstraintValidator<LoanAmountWithinSalaryLimit, ScoringDataDto> {
    @Override
    public boolean isValid(ScoringDataDto value, ConstraintValidatorContext context) {

        boolean valid = value.getAmount().compareTo(value.getEmployment().getSalary().multiply(BigDecimal.valueOf(24))) <= 0;
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("amount")
                    .addConstraintViolation();
        }
        return valid;

    }
}

