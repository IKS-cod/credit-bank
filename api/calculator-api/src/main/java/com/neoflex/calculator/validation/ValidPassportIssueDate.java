package com.neoflex.calculator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassportIssueDateValidator.class)
@Documented
public @interface ValidPassportIssueDate {
    String message() default "Дата выдачи паспорта должна быть не раньше 14 лет после рождения и не позже 120 лет после рождения, а также не в будущем";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

