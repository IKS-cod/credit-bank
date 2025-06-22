package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ScoringBirthdateBorrower {

    /**
     * Проверяет, что возраст заемщика от 20 до 65 лет включительно.
     *
     * @param birthdate дата рождения
     * @return true, если возраст в диапазоне [20, 65], иначе false
     */
    public boolean scoringBirthdateBorrower(LocalDate birthdate) {

        LocalDate today = LocalDate.now();
        int age = Period.between(birthdate, today).getYears();

        return age >= 20 && age <= 65;
    }
}

