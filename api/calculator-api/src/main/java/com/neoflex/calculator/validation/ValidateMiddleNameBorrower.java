package com.neoflex.calculator.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidateMiddleNameBorrower {
    private final Pattern namePattern;

    public ValidateMiddleNameBorrower(@Value("${validation.name.pattern}") String patternStr) {
        this.namePattern = Pattern.compile(patternStr);
    }

    public boolean validateMiddleNameBorrower(String middleName) {
        if (middleName != null && !middleName.isEmpty()) {
            if (!namePattern.matcher(middleName).matches()) {
                return false;
            }
        }
        return true;
    }
}
