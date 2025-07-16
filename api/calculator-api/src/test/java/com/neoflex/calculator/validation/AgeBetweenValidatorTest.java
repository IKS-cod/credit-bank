package com.neoflex.calculator.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;


import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AgeBetweenValidatorTest {

    private AgeBetweenValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new AgeBetweenValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);

        // Инициализация с минимальным и максимальным возрастом
        AgeBetween annotation = new AgeBetween() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return AgeBetween.class;
            }

            @Override
            public String message() {
                return "Возраст должен быть между 18 и 65";
            }

            @Override
            public Class<?>[] groups() {
                return new Class<?>[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public int min() {
                return 18;
            }

            @Override
            public int max() {
                return 65;
            }
        };

        validator.initialize(annotation);
    }

    @Test
    void shouldReturnTrue_whenAgeIsWithinRange() {
        LocalDate birthdate = LocalDate.now().minusYears(30);
        assertThat(validator.isValid(birthdate, context)).isTrue();

        birthdate = LocalDate.now().minusYears(18);
        assertThat(validator.isValid(birthdate, context)).isTrue();

        birthdate = LocalDate.now().minusYears(65);
        assertThat(validator.isValid(birthdate, context)).isTrue();
    }

    @Test
    void shouldReturnFalse_whenAgeIsBelowMin() {
        LocalDate birthdate = LocalDate.now().minusYears(17);
        assertThat(validator.isValid(birthdate, context)).isFalse();
    }

    @Test
    void shouldReturnFalse_whenAgeIsAboveMax() {
        LocalDate birthdate = LocalDate.now().minusYears(66);
        assertThat(validator.isValid(birthdate, context)).isFalse();
    }
}
