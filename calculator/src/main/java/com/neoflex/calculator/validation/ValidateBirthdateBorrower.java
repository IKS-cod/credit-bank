package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ValidateBirthdateBorrower {

    /**
     * Проверяет, что дата рождения не null и возраст не младше 18 лет на текущий день.
     *
     * @param birthdate дата рождения клиента
     * @return true, если возраст >= 18, иначе false
     */
    public boolean validateBirthdateBorrower(LocalDate birthdate) {
        if (birthdate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        if (birthdate.isAfter(today)) {
            return false;
        }
        int age = Period.between(birthdate, today).getYears();
        return age >= 18;
    }
}


