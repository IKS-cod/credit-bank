package com.neoflex.calculator.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import com.neoflex.calculator.dto.EmploymentDto;
import com.neoflex.calculator.dto.ScoringDataDto;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoanAmountWithinSalaryLimitValidatorTest {

    private LoanAmountWithinSalaryLimitValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder violationBuilder;

    @Mock
    private NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new LoanAmountWithinSalaryLimitValidator();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenAmountIsWithinLimit() {
        ScoringDataDto dto = new ScoringDataDto();
        dto.setAmount(BigDecimal.valueOf(1000));

        EmploymentDto employment = new EmploymentDto();
        employment.setSalary(BigDecimal.valueOf(50));
        dto.setEmployment(employment);

        boolean result = validator.isValid(dto, context);

        assertThat(result).isTrue();
        verify(context, never()).disableDefaultConstraintViolation();
    }
}


