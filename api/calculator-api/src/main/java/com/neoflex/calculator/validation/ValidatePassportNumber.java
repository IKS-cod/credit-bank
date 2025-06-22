package com.neoflex.calculator.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidatePassportNumber {
    private final Pattern passportNumberPattern;

    public ValidatePassportNumber(@Value("${validation.passport.number.pattern}") String patternStr) {
        this.passportNumberPattern = Pattern.compile(patternStr);
    }

    public boolean validatePassportNumber(String number) {
        boolean valid = true;
        if (number == null || !passportNumberPattern.matcher(number).matches()) {
            valid = false;
        }
        return valid;
    }
}
