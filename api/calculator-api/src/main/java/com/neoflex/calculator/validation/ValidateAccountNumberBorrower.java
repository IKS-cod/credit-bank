package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidateAccountNumberBorrower {

    /**
     * Проверяет, что номер счета не null, состоит ровно из 20 цифр.
     */
    public boolean validateAccountNumberBorrower(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            return false;
        }
        // Проверка на 20 цифр
        return accountNumber.matches("^\\d{20}$");
    }
}

