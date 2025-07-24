package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO для запроса на оформление кредитной заявки")
public class LoanStatementRequestDto {

    @Schema(description = "Сумма кредита, минимум 20000", example = "50000.00", required = true)
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах, минимум 6", example = "12", required = true)
    private Integer term;

    @Schema(description = "Имя клиента (латинские буквы, от 2 до 30 символов)", example = "John", required = true)
    private String firstName;

    @Schema(description = "Фамилия клиента (латинские буквы, от 2 до 30 символов)", example = "Doe", required = true)
    private String lastName;

    @Schema(description = "Отчество клиента (латинские буквы, от 2 до 30 символов)", example = "Michael")
    private String middleName;

    @Schema(description = "Email адрес клиента", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Дата рождения клиента (формат гггг-мм-дд), не младше 18 лет", example = "1990-05-15", required = true)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта (4 цифры)", example = "1234", required = true)
    private String passportSeries;

    @Schema(description = "Номер паспорта (6 цифр)", example = "567890", required = true)
    private String passportNumber;
}
