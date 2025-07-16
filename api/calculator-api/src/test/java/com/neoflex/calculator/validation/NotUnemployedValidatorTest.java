package com.neoflex.calculator.validation;

import com.neoflex.calculator.enums.EmploymentStatus;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class NotUnemployedValidatorTest {

    private NotUnemployedValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new NotUnemployedValidator();
    }

    @Test
    void isValid_ShouldReturnFalse_IfStatusIsUnemployed() {
        boolean result = validator.isValid(EmploymentStatus.UNEMPLOYED, context);
        assertThat(result).isFalse();
    }

    @Test
    void isValid_ShouldReturnTrue_IfStatusIsSelfEmployed() {
        boolean result = validator.isValid(EmploymentStatus.SELF_EMPLOYED, context);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_ShouldReturnTrue_IfStatusIsBusinessOwner() {
        boolean result = validator.isValid(EmploymentStatus.BUSINESS_OWNER, context);
        assertThat(result).isTrue();
    }
}

