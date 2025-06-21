package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.EmploymentStatus;
import org.springframework.stereotype.Component;

@Component
public class ValidateEmploymentStatusBorrower {

    /**
     * Проверяет, что employmentStatus не null.
     */
    public boolean validateEmploymentStatusBorrower(EmploymentStatus employmentStatus) {
        return employmentStatus != null;
    }
}