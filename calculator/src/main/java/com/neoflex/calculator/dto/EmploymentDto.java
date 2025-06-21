package com.neoflex.calculator.dto;

import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "DTO для информации о трудоустройстве клиента")
public class EmploymentDto {

    @Schema(description = "Статус занятости", example = "SELF_EMPLOYED", required = true)
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7707083893")
    private String employerINN;

    @Schema(description = "Заработная плата", example = "75000.00")
    private BigDecimal salary;

    @Schema(description = "Должность", example = "MIDDLE_MANAGER")
    private Position position;

    @Schema(description = "Общий трудовой стаж в месяцах", example = "120")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий трудовой стаж на последнем месте работы в месяцах", example = "24")
    private Integer workExperienceCurrent;
}



