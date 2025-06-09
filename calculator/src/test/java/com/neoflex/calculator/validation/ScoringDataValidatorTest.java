package com.neoflex.calculator.validation;

import com.neoflex.calculator.dto.EmploymentDto;
import com.neoflex.calculator.dto.ScoringDataDto;
import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.MaritalStatus;
import com.neoflex.calculator.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;

class ScoringDataValidatorTest {

    private ScoringDataValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ScoringDataValidator();
    }

    @Test
    void validate_ShouldReturnFalse_WhenDtoIsNull() {
        assertFalse(validator.validate(null));
    }

    @Test
    void validate_ShouldReturnFalse_WhenFirstNameInvalid() {
        var dto = createValidDto();
        dto.setFirstName("A"); // меньше 2 символов
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenLastNameInvalid() {
        var dto = createValidDto();
        dto.setLastName("123"); // не латинские буквы
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenMiddleNameInvalid() {
        var dto = createValidDto();
        dto.setMiddleName("1"); // невалидное отчество
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenAmountLessThan20000() {
        var dto = createValidDto();
        dto.setAmount(BigDecimal.valueOf(19999));
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenTermLessThan6() {
        var dto = createValidDto();
        dto.setTerm(5);
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenBirthdateUnder18() {
        var dto = createValidDto();
        dto.setBirthdate(LocalDate.now().minusYears(17));
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenPassportSeriesInvalid() {
        var dto = createValidDto();
        dto.setPassportSeries("12A4");
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenPassportNumberInvalid() {
        var dto = createValidDto();
        dto.setPassportNumber("12345"); // меньше 6 цифр
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnFalse_WhenEmploymentIsNull() {
        var dto = createValidDto();
        dto.setEmployment(null);
        assertFalse(validator.validate(dto));
    }

    @Test
    void validate_ShouldReturnTrue_WhenAllValid() {
        var dto = createValidDto();
        assertTrue(validator.validate(dto));
    }

    // Вспомогательный метод для создания валидного DTO
    private ScoringDataDto createValidDto() {
        ScoringDataDto dto = new ScoringDataDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("Michael");
        dto.setAmount(BigDecimal.valueOf(50000));
        dto.setTerm(12);
        dto.setBirthdate(LocalDate.now().minusYears(25));
        dto.setGender(Gender.MALE);
        dto.setMaritalStatus(MaritalStatus.MARRIED);
        dto.setPassportSeries("1234");
        dto.setPassportNumber("123456");
        dto.setPassportIssueDate(LocalDate.now().minusYears(10));
        dto.setPassportIssueBranch("ОВД Центрального района");
        dto.setIsInsuranceEnabled(true);
        dto.setIsSalaryClient(false);
        dto.setDependentAmount(2);
        dto.setAccountNumber("40817810099910004312");

        EmploymentDto employment = new EmploymentDto();
        employment.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        employment.setEmployerINN("7707083893");
        employment.setSalary(BigDecimal.valueOf(75000));
        employment.setPosition(Position.MIDDLE_MANAGER);
        employment.setWorkExperienceTotal(120);
        employment.setWorkExperienceCurrent(24);
        dto.setEmployment(employment);

        return dto;
    }
}
