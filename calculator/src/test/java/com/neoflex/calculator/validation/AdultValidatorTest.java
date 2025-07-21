package com.neoflex.calculator.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdultValidatorTest {

    private AdultValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AdultValidator();
    }

    @Test
    void shouldReturnTrue_whenBirthdateIs18YearsAgoOrMore() {
        LocalDate birthdate = LocalDate.now().minusYears(18);
        assertThat(validator.isValid(birthdate, null)).isTrue();

        birthdate = LocalDate.now().minusYears(30);
        assertThat(validator.isValid(birthdate, null)).isTrue();
    }

    @Test
    void shouldReturnFalse_whenBirthdateIsLessThan18YearsAgo() {
        LocalDate birthdate = LocalDate.now().minusYears(17).plusDays(1);
        assertThat(validator.isValid(birthdate, null)).isFalse();

        birthdate = LocalDate.now();
        assertThat(validator.isValid(birthdate, null)).isFalse();
    }
}
