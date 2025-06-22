package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.Gender;
import org.springframework.stereotype.Component;

@Component
public class ValidateGenderBorrower {

    public boolean validateGender(Gender gender) {
        return gender != null;
    }
}
