package com.neoflex.calculator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeBetweenValidator.class)
@Documented
public @interface AgeBetween {
    String message() default "Отказ в кредите: Возраст должен быть в диапазоне от {min} до {max} лет";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 20;

    int max() default 65;
}

