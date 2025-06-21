package com.neoflex.calculator.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidateEmailBorrower {
    private final Pattern emailPattern;

    public ValidateEmailBorrower(@Value("${validation.email.pattern}") String emailPatternStr) {
        this.emailPattern = Pattern.compile(emailPatternStr);
    }

    public boolean validateEmailBorrower(String email) {
        if (email == null || !emailPattern.matcher(email).matches()) {
            return false;
        }
        return true;
    }

}
