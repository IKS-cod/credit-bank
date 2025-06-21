package com.neoflex.calculator.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidateFirstNameBorrower {
    private final Pattern namePattern;

    public ValidateFirstNameBorrower(@Value("${validation.name.pattern}") String patternStr) {
        this.namePattern = Pattern.compile(patternStr);
    }

    public boolean validateFirstNameBorrower(String value) {
        if (value == null || !namePattern.matcher(value).matches()) {
            return false;
        }
        return true;
    }

}
