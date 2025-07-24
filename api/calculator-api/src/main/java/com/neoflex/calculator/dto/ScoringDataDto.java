package com.neoflex.calculator.dto;

import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.MaritalStatus;
import com.neoflex.calculator.validation.Adult;
import com.neoflex.calculator.validation.AgeBetween;
import com.neoflex.calculator.validation.LoanAmountWithinSalaryLimit;
import com.neoflex.calculator.validation.ValidPassportIssueDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO для данных, используемых при скоринге кредитной заявки")
@ValidPassportIssueDate
@LoanAmountWithinSalaryLimit
public class ScoringDataDto {

    @Schema(description = "Сумма кредита", example = "50000.00", required = true)
    @NotNull(message = "Сумма кредита обязательна")
    @DecimalMin(value = "20000", inclusive = true, message = "Сумма кредита должна быть не меньше 20000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12", required = true)
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

    @Schema(description = "Пол клиента", example = "MALE", required = true)
    @NotNull(message = "Поле: 'Пол клиента' должно быть заполнено")
    private Gender gender;

    @Schema(description = "Дата рождения клиента (формат гггг-мм-дд), не младше 18 лет", example = "1990-05-15", required = true)
    @NotNull(message = "Дата рождения клиента обязательна")
    @Adult(message = "Возраст клиента должен быть не младше 18 лет")
    @AgeBetween(min = 20, max = 65)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта (4 цифры)", example = "1234", required = true)
    @NotBlank(message = "Серия паспорта обязательна")
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна быть (4 цифры)")
    private String passportSeries;

    @Schema(description = "Номер паспорта (6 цифр)", example = "567890", required = true)
    @NotBlank(message = "Номер паспорта обязателен")
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен быть (6 цифр)")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта (формат гггг-мм-дд)", example = "2010-06-01", required = true)
    @NotNull(message = "Дата выдачи паспорта обязательна")
    @PastOrPresent(message = "Дата выдачи паспорта не может быть в будущем")
    private LocalDate passportIssueDate;

    @Schema(description = "Кем выдан паспорт", example = "ОВД Центрального района", required = true)
    @NotBlank(message = "Поле 'Кем выдан паспорт' обязательно для заполнения")
    @Size(min = 2, max = 255, message = "Длина поля должна быть от 2 до 255 символов")
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "MARRIED", required = true)
    @NotNull(message = "Поле: 'Семейное положение' должно быть заполнено")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "2", required = true)
    @NotNull(message = "Количество иждивенцев обязательно для заполнения")
    @Min(value = 0, message = "Количество иждивенцев не может быть отрицательным")
    private Integer dependentAmount;

    @Valid
    @Schema(description = "Информация о трудоустройстве", required = true)
    private EmploymentDto employment;

    @Schema(description = "Номер банковского счета", example = "40817810099910004312", required = true)
    @NotBlank(message = "Номер банковского счета обязателен")
    @Pattern(regexp = "\\d{20}", message = "Номер счета должен содержать ровно 20 цифр")
    private String accountNumber;

    @Schema(description = "Включена ли страховка", example = "true", required = true)
    @NotNull(message = "Поле 'Страховка включена' обязательно для заполнения")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли клиент зарплатным", example = "false", required = true)
    @NotNull(message = "Поле 'Является ли клиент зарплатным' обязательно для заполнения")
    private Boolean isSalaryClient;
}


