package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Элемент графика платежей по кредиту")
public class PaymentScheduleElementDto {

    @Schema(description = "Номер платежа в графике", example = "1", required = true)
    private Integer number;

    @Schema(description = "Дата платежа", example = "2025-07-01", required = true)
    private LocalDate date;

    @Schema(description = "Общая сумма платежа", example = "4500.00", required = true)
    private BigDecimal totalPayment;

    @Schema(description = "Сумма процентов в платеже", example = "500.00", required = true)
    private BigDecimal interestPayment;

    @Schema(description = "Сумма погашения основного долга", example = "4000.00", required = true)
    private BigDecimal debtPayment;

    @Schema(description = "Остаток основного долга после платежа", example = "46000.00", required = true)
    private BigDecimal remainingDebt;
}
