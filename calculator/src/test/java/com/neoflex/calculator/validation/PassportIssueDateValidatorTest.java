package com.neoflex.calculator.validation;

import com.neoflex.calculator.dto.ScoringDataDto;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassportIssueDateValidatorTest {

    private PassportIssueDateValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder violationBuilder;

    @Mock
    private NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {
        validator = new PassportIssueDateValidator();
    }

    @Test
    void isValid_ShouldReturnTrue_WhenPassportIssueDateIsValid() {
        ScoringDataDto dto = new ScoringDataDto();
        dto.setBirthdate(LocalDate.now().minusYears(20));
        dto.setPassportIssueDate(LocalDate.now().minusYears(5));

        boolean result = validator.isValid(dto, context);

        assertThat(result).isTrue();
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenPassportIssueDateBeforeMinAge() {
        when(context.getDefaultConstraintMessageTemplate()).thenReturn("some message");
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

        ScoringDataDto dto = new ScoringDataDto();
        dto.setBirthdate(LocalDate.now().minusYears(20));
        dto.setPassportIssueDate(LocalDate.now().minusYears(10));

        boolean result = validator.isValid(dto, context);

        assertThat(result).isFalse();
        verify(context, times(1)).disableDefaultConstraintViolation();
        verify(context, times(1)).buildConstraintViolationWithTemplate(anyString());
        verify(violationBuilder, times(1)).addPropertyNode("passportIssueDate");
        verify(nodeBuilder, times(1)).addConstraintViolation();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenPassportIssueDateInFuture() {
        when(context.getDefaultConstraintMessageTemplate()).thenReturn("some message");
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

        ScoringDataDto dto = new ScoringDataDto();
        dto.setBirthdate(LocalDate.now().minusYears(30));
        dto.setPassportIssueDate(LocalDate.now().plusDays(1));

        boolean result = validator.isValid(dto, context);

        assertThat(result).isFalse();
        verify(context, times(1)).disableDefaultConstraintViolation();
        verify(context, times(1)).buildConstraintViolationWithTemplate(anyString());
        verify(violationBuilder, times(1)).addPropertyNode("passportIssueDate");
        verify(nodeBuilder, times(1)).addConstraintViolation();
    }

    @Test
    void isValid_ShouldReturnFalse_WhenPassportIssueDateAfterMaxAge() {
        when(context.getDefaultConstraintMessageTemplate()).thenReturn("some message");
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

        ScoringDataDto dto = new ScoringDataDto();
        dto.setBirthdate(LocalDate.now().minusYears(130));
        dto.setPassportIssueDate(LocalDate.now().minusYears(5));

        boolean result = validator.isValid(dto, context);

        assertThat(result).isFalse();
        verify(context, times(1)).disableDefaultConstraintViolation();
        verify(context, times(1)).buildConstraintViolationWithTemplate(anyString());
        verify(violationBuilder, times(1)).addPropertyNode("passportIssueDate");
        verify(nodeBuilder, times(1)).addConstraintViolation();
    }
}
