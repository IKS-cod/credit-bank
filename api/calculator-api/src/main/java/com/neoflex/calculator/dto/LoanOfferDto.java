package com.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "DTO для предложения по кредиту")
public class LoanOfferDto {

    @Schema(description = "Уникальный идентификатор заявки", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID statementId;

    @Schema(description = "Запрошенная сумма кредита", example = "50000.00", required = true)
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма к возврату", example = "55000.00")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита в месяцах", example = "12", required = true)
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "4583.33")
    private BigDecimal monthlyPayment;

    @Schema(description = "Процентная ставка по кредиту", example = "12.5")
    private BigDecimal rate;

    @Schema(description = "Включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли клиент зарплатным", example = "false")
    private Boolean isSalaryClient;
}



