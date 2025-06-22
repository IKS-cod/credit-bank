package com.neoflex.deal.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class PaymentSchedule {

    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;

    public PaymentSchedule() {
    }

    public PaymentSchedule(Integer number, LocalDate date, BigDecimal totalPayment,
                                     BigDecimal interestPayment, BigDecimal debtPayment,
                                     BigDecimal remainingDebt) {
        this.number = number;
        this.date = date;
        this.totalPayment = totalPayment;
        this.interestPayment = interestPayment;
        this.debtPayment = debtPayment;
        this.remainingDebt = remainingDebt;
    }

}

