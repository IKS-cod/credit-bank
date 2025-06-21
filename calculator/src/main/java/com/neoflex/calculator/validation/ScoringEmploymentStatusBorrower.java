package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.EmploymentStatus;
import org.springframework.stereotype.Component;

import static com.neoflex.calculator.enums.EmploymentStatus.UNEMPLOYED;

@Component
public class ScoringEmploymentStatusBorrower {
    public boolean scoringEmploymentStatusBorrower(EmploymentStatus employmentStatus) {
        if(employmentStatus.equals(UNEMPLOYED)){
            return false;
        }
        return true;
    }
}
