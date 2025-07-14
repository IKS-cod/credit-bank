package com.neoflex.calculator.validation;

import com.neoflex.calculator.dto.ScoringDataDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PassportIssueDateValidator implements ConstraintValidator<ValidPassportIssueDate, ScoringDataDto> {

    @Override
    public boolean isValid(ScoringDataDto dto, ConstraintValidatorContext context) {
        LocalDate birthdate = dto.getBirthdate();
        LocalDate passportIssueDate = dto.getPassportIssueDate();

        LocalDate minIssueDate = birthdate.plusYears(14);
        LocalDate maxIssueDate = birthdate.plusYears(120);
        LocalDate today = LocalDate.now();

        boolean notBefore14 = !passportIssueDate.isBefore(minIssueDate);
        boolean notAfter120 = !passportIssueDate.isAfter(maxIssueDate);
        boolean notInFuture = !passportIssueDate.isAfter(today);

        boolean valid = notBefore14 && notAfter120 && notInFuture;

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("passportIssueDate")
                    .addConstraintViolation();
        }

        return valid;

    }
}

