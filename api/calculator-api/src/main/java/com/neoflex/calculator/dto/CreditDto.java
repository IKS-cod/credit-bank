package com.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "DTO для передачи параметров кредита")
public class CreditDto {

    @Schema(description = "Сумма кредита", example = "50000.00", required = true)
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12", required = true)
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "4500.50")
    private BigDecimal monthlyPayment;

    @Schema(description = "Процентная ставка", example = "12.5")
    private BigDecimal rate;

    @Schema(description = "Полная стоимость кредита (ПСК)", example = "15.0")
    private BigDecimal psk;

    @Schema(description = "Включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли клиент зарплатным", example = "false")
    private Boolean isSalaryClient;

    @Schema(description = "График платежей")
    private List<PaymentScheduleElementDto> paymentSchedule;
}


