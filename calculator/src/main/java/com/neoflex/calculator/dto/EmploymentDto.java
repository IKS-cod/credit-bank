package com.neoflex.calculator.dto;

import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Position;
import com.neoflex.calculator.validation.NotUnemployed;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "DTO для информации о трудоустройстве клиента")
public class EmploymentDto {

    @Schema(description = "Статус занятости", example = "SELF_EMPLOYED", required = true)
    @NotNull(message = "Поле: 'Статус занятости' должно быть заполнено")
    @NotUnemployed
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7707083893", required = true)
    @NotBlank(message = "ИНН работодателя обязателен")
    @Pattern(regexp = "\\d{10}|\\d{12}", message = "ИНН должен содержать 10 или 12 цифр")
    private String employerINN;

    @Schema(description = "Заработная плата", example = "75000.00", required = true)
    @NotNull(message = "Заработная плата обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Заработная плата должна быть больше 0")
    private BigDecimal salary;

    @Schema(description = "Должность", example = "MIDDLE_MANAGER", required = true)
    @NotNull(message = "Поле: 'Должность' должно быть заполнено")
    private Position position;

    @Schema(description = "Общий трудовой стаж в месяцах", example = "120", required = true)
    @NotNull(message = "Общий трудовой стаж обязателен")
    @Min(value = 18, message = "Общий трудовой стаж менее 18 месяцев — отказ в выдаче кредита")
    @Max(value = 600, message = "Общий трудовой стаж не может превышать 600 месяцев")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий трудовой стаж на последнем месте работы в месяцах", example = "24", required = true)
    @NotNull(message = "Текущий трудовой стаж обязателен")
    @Min(value = 3, message = "Текущий трудовой стаж менее 3 месяцев — отказ в выдаче кредита")
    @Max(value = 600, message = "Текущий трудовой стаж не может превышать 600 месяцев")
    private Integer workExperienceCurrent;
}



