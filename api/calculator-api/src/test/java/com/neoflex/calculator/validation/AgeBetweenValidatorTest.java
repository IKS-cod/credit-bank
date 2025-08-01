package com.neoflex.calculator.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.time.LocalDate;


import com.neoflex.calculator.exception.AgeRestrictionException;
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
                return "Возраст должен быть между 20 и 65";
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
                return 20;
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

        birthdate = LocalDate.now().minusYears(20);
        assertThat(validator.isValid(birthdate, context)).isTrue();

        birthdate = LocalDate.now().minusYears(65);
        assertThat(validator.isValid(birthdate, context)).isTrue();
    }

    @Test
    void shouldThrow_whenAgeIsBelowMin() {
        LocalDate birthdate = LocalDate.now().minusYears(17);
        assertThatThrownBy(() -> validator.isValid(birthdate, null))
                .isInstanceOf(AgeRestrictionException.class)
                .hasMessageContaining("Отказ в кредите: Возраст должен быть в диапазоне от 20 до 65 лет");
    }

    @Test
    void shouldThrow_whenAgeIsAboveMax() {
        LocalDate birthdate = LocalDate.now().minusYears(66);
        assertThatThrownBy(() -> validator.isValid(birthdate, null))
                .isInstanceOf(AgeRestrictionException.class)
                .hasMessageContaining("Отказ в кредите: Возраст должен быть в диапазоне от 20 до 65 лет");
    }

    @Test
    void shouldNotThrow_whenAgeWithinBounds() {
        LocalDate birthdate = LocalDate.now().minusYears(30);
        boolean result = validator.isValid(birthdate, null);
        assertThat(result).isTrue();
    }
}
