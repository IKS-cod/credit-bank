package com.neoflex.deal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdultValidator.class)
@Documented
public @interface Adult {
    String message() default "Возраст должен быть не меньше 18 лет";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
