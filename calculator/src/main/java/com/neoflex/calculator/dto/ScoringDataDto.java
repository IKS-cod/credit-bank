package com.neoflex.calculator.dto;

import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO для данных, используемых при скоринге кредитной заявки")
public class ScoringDataDto {

    @Schema(description = "Сумма кредита", example = "50000.00", required = true)
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12", required = true)
    private Integer term;

    @Schema(description = "Имя клиента (латинские буквы, от 2 до 30 символов)", example = "John", required = true)
    private String firstName;

    @Schema(description = "Фамилия клиента (латинские буквы, от 2 до 30 символов)", example = "Doe", required = true)
    private String lastName;

    @Schema(description = "Отчество клиента (латинские буквы, от 2 до 30 символов)", example = "Michael")
    private String middleName;

    @Schema(description = "Пол клиента", example = "MALE, FEMALE, NON_BINARY", required = true)
    private Gender gender;

    @Schema(description = "Дата рождения клиента (формат гггг-мм-дд), не младше 18 лет", example = "1990-05-15", required = true)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта (4 цифры)", example = "1234", required = true)
    private String passportSeries;

    @Schema(description = "Номер паспорта (6 цифр)", example = "567890", required = true)
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта (формат гггг-мм-дд)", example = "2010-06-01", required = true)
    private LocalDate passportIssueDate;

    @Schema(description = "Кем выдан паспорт", example = "ОВД Центрального района", required = true)
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "MARRIED, DIVORCED")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "2")
    private Integer dependentAmount;

    @Schema(description = "Информация о трудоустройстве")
    private EmploymentDto employment;

    @Schema(description = "Номер банковского счета", example = "40817810099910004312")
    private String accountNumber;

    @Schema(description = "Включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли клиент зарплатным", example = "false")
    private Boolean isSalaryClient;
}


