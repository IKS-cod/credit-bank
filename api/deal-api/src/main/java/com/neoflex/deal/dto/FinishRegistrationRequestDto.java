package com.neoflex.deal.dto;

import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO для завершения регистрации клиента.
 */
@Data
@Schema(description = "DTO для передачи сведений о завершающем этапе регистрации клиента")
public class FinishRegistrationRequestDto {

    @Schema(description = "Пол клиента", example = "MALE", required = true)
    private Gender gender;

    @Schema(description = "Семейное положение", example = "MARRIED", required = true)
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "2", required = true)
    private Integer dependentAmount;

    @Schema(description = "Дата выдачи паспорта", example = "2015-01-10", required = true)
    private LocalDate passportIssueDate;

    @Schema(description = "Код подразделения, выдавшего паспорт", example = "770-053", required = true)
    private String passportIssueBranch;

    @Schema(description = "Информация о трудоустройстве", required = true)
    private EmploymentDto employment;

    @Schema(description = "Номер банковского счета", example = "40817810099910004312", required = true)
    private String accountNumber;
}

