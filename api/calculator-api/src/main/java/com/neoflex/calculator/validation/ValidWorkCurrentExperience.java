package com.neoflex.calculator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WorkCurrentExperienceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWorkCurrentExperience {
    String message() default "Текущий трудовой стаж должен быть от {min} до {max} месяцев";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int min() default 3;
    int max() default 600;
}

