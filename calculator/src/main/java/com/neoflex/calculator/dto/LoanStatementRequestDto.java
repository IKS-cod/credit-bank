package com.neoflex.calculator.dto;

import com.neoflex.calculator.validation.Adult;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO для запроса на оформление кредитной заявки")
public class LoanStatementRequestDto {

    @Schema(description = "Сумма кредита, минимум 20000", example = "50000.00", required = true)
    @NotNull(message = "Сумма кредита обязательна")
    @DecimalMin(value = "20000", inclusive = true, message = "Сумма кредита должна быть не меньше 20000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах, минимум 6", example = "12", required = true)
    @NotNull(message = "Срок кредита обязателен")
    @Min(value = 6, message = "Срок кредита должен быть не меньше 6 месяцев")
    private Integer term;

    @Schema(description = "Имя клиента (латинские буквы, от 2 до 30 символов)", example = "John", required = true)
    @NotBlank(message = "Поле: Имя  обязательно для заполнения")
    @Size(min = 2, max = 30, message = "Длина должна быть от 2 до 30 символов")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Допустимы только латинские буквы")
    private String firstName;

    @Schema(description = "Фамилия клиента (латинские буквы, от 2 до 30 символов)", example = "Doe", required = true)
    @NotBlank(message = "Поле: Фамилия  обязательно для заполнения")
    @Size(min = 2, max = 30, message = "Длина должна быть от 2 до 30 символов")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Допустимы только латинские буквы")
    private String lastName;

    @Schema(description = "Отчество клиента (латинские буквы, от 2 до 30 символов)", example = "Michael")
    @Size(min = 2, max = 30, message = "Длина должна быть от 2 до 30 символов")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Допустимы только латинские буквы")
    private String middleName;

    @Schema(description = "Email адрес клиента", example = "john.doe@example.com", required = true)
    @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$", message = "Email должен быть формата: john.doe@example.com")
    private String email;

    @Schema(description = "Дата рождения клиента (формат гггг-мм-дд), не младше 18 лет", example = "1990-05-15", required = true)
    @NotNull(message = "Дата рождения клиента обязательна")
    @Adult(message = "Возраст клиента должен быть не младше 18 лет")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта (4 цифры)", example = "1234", required = true)
    @NotBlank(message = "Серия паспорта обязательна")
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна быть (4 цифры)")
    private String passportSeries;

    @Schema(description = "Номер паспорта (6 цифр)", example = "567890", required = true)
    @NotBlank(message = "Номер паспорта обязателен")
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен быть(6 цифр)")
    private String passportNumber;
}


