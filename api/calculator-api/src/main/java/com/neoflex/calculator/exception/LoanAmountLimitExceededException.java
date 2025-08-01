package com.neoflex.calculator.exception;

public class LoanAmountLimitExceededException extends RuntimeException {
    public LoanAmountLimitExceededException(String message) {
        super(message);
    }
}

