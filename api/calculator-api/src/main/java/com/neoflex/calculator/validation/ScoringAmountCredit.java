package com.neoflex.calculator.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ScoringAmountCredit {

    /**
     * Проверяет, что сумма займа не превышает 24-кратный размер зарплаты.
     *
     * @param amount сумма займа
     * @param salary зарплата заемщика
     * @return true, если сумма займа допустима, false — если превышает 24 зарплаты или данные некорректны
     */
    public boolean scoringAmountCredit(BigDecimal amount, BigDecimal salary) {

        BigDecimal maxAmount = salary.multiply(BigDecimal.valueOf(24));
        return amount.compareTo(maxAmount) <= 0;
    }
}

