package com.neoflex.deal.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class LoanOffer {

    private UUID statementId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    public LoanOffer() {
    }

    public LoanOffer(UUID statementId, BigDecimal requestedAmount, BigDecimal totalAmount,
                        Integer term, BigDecimal monthlyPayment, BigDecimal rate,
                        Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        this.statementId = statementId;
        this.requestedAmount = requestedAmount;
        this.totalAmount = totalAmount;
        this.term = term;
        this.monthlyPayment = monthlyPayment;
        this.rate = rate;
        this.isInsuranceEnabled = isInsuranceEnabled;
        this.isSalaryClient = isSalaryClient;
    }

}

