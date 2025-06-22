package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidatePassportIssueBranchBorrower {

    /**
     * Проверяет, что строка не null, не пустая и не состоит из одних пробелов.
     */
    public boolean validatePassportIssueBranchBorrower(String passportIssueBranch) {
        return passportIssueBranch != null && !passportIssueBranch.trim().isEmpty();
    }
}

