package com.neoflex.calculator.controller;

import com.neoflex.calculator.dto.*;
import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.Position;
import com.neoflex.calculator.exception.*;
import com.neoflex.calculator.service.CalculatorService;
import com.neoflex.calculator.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.neoflex.calculator.enums.EmploymentStatus.SELF_EMPLOYED;
import static com.neoflex.calculator.enums.MaritalStatus.MARRIED;
import static com.neoflex.calculator.enums.Position.MIDDLE_MANAGER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for CalculatorController")
class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @Mock
    private ValidatePassportSeries validatePassportSeries;
    @Mock
    private ValidatePassportNumber validatePassportNumber;
    @Mock
    private ValidateEmailBorrower validateEmailBorrower;
    @Mock
    private ValidateBirthdateBorrower validateBirthdateBorrower;
    @Mock
    private ValidateTermCredit validateTermCredit;
    @Mock
    private ValidateAmountCredit validateAmountCredit;
    @Mock
    private ValidateMiddleNameBorrower validateMiddleNameBorrower;
    @Mock
    private ValidateLastNameBorrower validateLastNameBorrower;
    @Mock
    private ValidateFirstNameBorrower validateFirstNameBorrower;
    @Mock
    private ValidateGenderBorrower validateGenderBorrower;
    @Mock
    private ValidatePassportIssueDateBorrower validatePassportIssueDateBorrower;
    @Mock
    private ValidatePassportIssueBranchBorrower validatePassportIssueBranchBorrower;
    @Mock
    private ValidateMaritalStatusBorrower validateMaritalStatusBorrower;
    @Mock
    private ValidateDependentAmountBorrower validateDependentAmountBorrower;
    @Mock
    private ValidateEmploymentStatusBorrower validateEmploymentStatusBorrower;
    @Mock
    private ValidateEmployerINNBorrower validateEmployerINNBorrower;
    @Mock
    private ValidateSalaryBorrower validateSalaryBorrower;
    @Mock
    private ValidatePositionBorrower validatePositionBorrower;
    @Mock
    private ValidateWorkExperienceTotalBorrower validateWorkExperienceTotalBorrower;
    @Mock
    private ValidateWorkExperienceCurrentBorrower validateWorkExperienceCurrentBorrower;
    @Mock
    private ValidateAccountNumberBorrower validateAccountNumberBorrower;
    @Mock
    private ValidateIsInsuranceEnabledBorrower validateIsInsuranceEnabledBorrower;
    @Mock
    private ValidateIsSalaryClientBorrower validateIsSalaryClientBorrower;

    @Mock
    private ScoringEmploymentStatusBorrower scoringEmploymentStatusBorrower;
    @Mock
    private ScoringAmountCredit scoringAmountCredit;
    @Mock
    private ScoringBirthdateBorrower scoringBirthdateBorrower;
    @Mock
    private ScoringWorkExperienceTotalBorrower scoringWorkExperienceTotalBorrower;
    @Mock
    private ScoringWorkExperienceCurrentBorrower scoringWorkExperienceCurrentBorrower;

    @InjectMocks
    private CalculatorController calculatorController;

    @Test
    @DisplayName("calculateOffers: success scenario returns offers")
    void calculateOffers_Success() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");

        List<LoanOfferDto> expectedOffers = Collections.singletonList(new LoanOfferDto());

        // По умолчанию все валидации возвращают true
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateAmountCredit.validateAmountCredit(any(BigDecimal.class))).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any(LocalDate.class))).thenReturn(true);
        when(validateEmailBorrower.validateEmailBorrower(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);

        when(calculatorService.calculateOffers(any(LoanStatementRequestDto.class))).thenReturn(expectedOffers);
        List<LoanOfferDto> result = calculatorController.calculateOffers(validRequest);

        assertNotNull(result);
        assertEquals(expectedOffers, result);

        verify(validateFirstNameBorrower).validateFirstNameBorrower(validRequest.getFirstName());
        verify(validateLastNameBorrower).validateLastNameBorrower(validRequest.getLastName());
        verify(validateMiddleNameBorrower).validateMiddleNameBorrower(validRequest.getMiddleName());
        verify(validateAmountCredit).validateAmountCredit(validRequest.getAmount());
        verify(validateTermCredit).validateTermCredit(validRequest.getTerm());
        verify(validateBirthdateBorrower).validateBirthdateBorrower(validRequest.getBirthdate());
        verify(validateEmailBorrower).validateEmailBorrower(validRequest.getEmail());
        verify(validatePassportNumber).validatePassportNumber(validRequest.getPassportNumber());
        verify(validatePassportSeries).validatePassportSeries(validRequest.getPassportSeries());

        verify(calculatorService).calculateOffers(validRequest);
    }

    @Test
    @DisplayName("calculateOffers: invalid first name throws IllegalFirstNameBorrowerException")
    void calculateOffers_InvalidFirstName_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(false);

        IllegalFirstNameBorrowerException ex = assertThrows(IllegalFirstNameBorrowerException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Имя заемщика должно быть 2-30 латинских букв"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid last name throws IllegalLastNameBorrowerException")
    void calculateOffers_InvalidLastName_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(false);

        IllegalLastNameBorrowerException ex = assertThrows(IllegalLastNameBorrowerException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Фамилия заемщика должна быть 2-30 латинских букв"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid middle name throws IllegalMiddleNameBorrowerException")
    void calculateOffers_InvalidMiddleName_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(false);

        IllegalMiddleNameBorrowerException ex = assertThrows(IllegalMiddleNameBorrowerException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Отчество заемщика должно быть 2-30 латинских букв"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid amount throws IllegalAmountCreditException")
    void calculateOffers_InvalidAmount_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(false);

        IllegalAmountCreditException ex = assertThrows(IllegalAmountCreditException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Ошибка Суммы: минимальное значение 20000"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid term throws IllegalTermCreditException")
    void calculateOffers_InvalidTerm_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(false);

        IllegalTermCreditException ex = assertThrows(IllegalTermCreditException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Ошибка Срока: минимальное значение 6 месяцев"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid birthdate throws IllegalBirthdateBorrowerException")
    void calculateOffers_InvalidBirthdate_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(false);

        IllegalBirthdateBorrowerException ex = assertThrows(IllegalBirthdateBorrowerException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("возраст должен быть ≥18 лет"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid email throws IllegalEmailBorrowerException")
    void calculateOffers_InvalidEmail_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validateEmailBorrower.validateEmailBorrower(anyString())).thenReturn(false);

        IllegalEmailBorrowerException ex = assertThrows(IllegalEmailBorrowerException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Email: неверный формат"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid passport number throws IllegalPassportNumberException")
    void calculateOffers_InvalidPassportNumber_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validateEmailBorrower.validateEmailBorrower(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(false);

        IllegalPassportNumberException ex = assertThrows(IllegalPassportNumberException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Номер паспорта должен содержать 6 цифр"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateOffers: invalid passport series throws IllegalPassportSeriesException")
    void calculateOffers_InvalidPassportSeries_Throws() {
        LoanStatementRequestDto validRequest = new LoanStatementRequestDto();
        validRequest.setFirstName("Ivan");
        validRequest.setLastName("Ivanov");
        validRequest.setMiddleName("Ivanovich");
        validRequest.setAmount(BigDecimal.valueOf(100000));
        validRequest.setTerm(12);
        validRequest.setBirthdate(LocalDate.of(1990, 1, 1));
        validRequest.setEmail("ivanov@example.com");
        validRequest.setPassportNumber("123456");
        validRequest.setPassportSeries("1234");
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validateEmailBorrower.validateEmailBorrower(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(false);

        IllegalPassportSeriesException ex = assertThrows(IllegalPassportSeriesException.class,
                () -> calculatorController.calculateOffers(validRequest));

        assertTrue(ex.getMessage().contains("Серия паспорта должна содержать 4 цифры"));
        verify(calculatorService, never()).calculateOffers(any());
    }

    @Test
    @DisplayName("calculateCredit: success scenario returns CreditDto")
    void calculateCredit_Success() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);

        EmploymentDto employment = new EmploymentDto();
        employment.setEmploymentStatus(SELF_EMPLOYED);
        employment.setEmployerINN("1234567890");
        employment.setSalary(BigDecimal.valueOf(50000));
        employment.setPosition(MIDDLE_MANAGER);
        employment.setWorkExperienceTotal(24);
        employment.setWorkExperienceCurrent(12);
        validScoringData.setEmployment(employment);

        CreditDto expectedCredit = new CreditDto();
        // По умолчанию все валидации и скоринги возвращают true
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(true);
        when(validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(anyBoolean())).thenReturn(true);

        when(scoringEmploymentStatusBorrower.scoringEmploymentStatusBorrower(any())).thenReturn(true);
        when(scoringAmountCredit.scoringAmountCredit(any(), any())).thenReturn(true);
        when(scoringBirthdateBorrower.scoringBirthdateBorrower(any())).thenReturn(true);
        when(scoringWorkExperienceTotalBorrower.scoringWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(scoringWorkExperienceCurrentBorrower.scoringWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);

        when(calculatorService.calculateCredit(any())).thenReturn(expectedCredit);

        CreditDto result = calculatorController.calculateCredit(validScoringData);
        assertNotNull(result);
        assertEquals(expectedCredit, result);
        verify(calculatorService).calculateCredit(validScoringData);
    }

    @Test
    void calculateCredit_InvalidAmount_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);

        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(false);
        assertThrows(IllegalAmountCreditException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidTerm_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(false);
        assertThrows(IllegalTermCreditException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidFirstName_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);

        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(false);
        assertThrows(IllegalFirstNameBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidLastName_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(false);
        assertThrows(IllegalLastNameBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidMiddleName_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(false);
        assertThrows(IllegalMiddleNameBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidGender_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(false);
        assertThrows(IllegalGenderBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidBirthdate_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(false);
        assertThrows(IllegalBirthdateBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidPassportSeries_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(false);
        assertThrows(IllegalPassportSeriesException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidPassportNumber_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(false);
        assertThrows(IllegalPassportNumberException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidPassportIssueDate_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(false);
        assertThrows(IllegalPassportIssueDateBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidPassportIssueBranch_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(false);
        assertThrows(IllegalPassportIssueBranchBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidMaritalStatus_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(false);
        assertThrows(IllegalMaritalStatusException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidDependentAmount_Throws() {
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(false);
        assertThrows(IllegalDependentAmountBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidEmploymentStatus_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // пример значения enum
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER); // пример значения enum
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(false);
        assertThrows(IllegalEmploymentStatusBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidEmployerINN_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // пример значения enum
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER); // пример значения enum
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(false);
        assertThrows(IllegalEmployerINNBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidSalary_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // пример значения enum
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER); // пример значения enum
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(false);
        assertThrows(IllegalSalaryBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidPosition_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // пример значения enum
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER); // пример значения enum
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(false);
        assertThrows(IllegalPositionBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidWorkExperienceTotal_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // пример значения enum
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER); // пример значения enum
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(false);
        assertThrows(IllegalWorkExperienceTotalBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidWorkExperienceCurrent_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // пример значения enum
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER); // пример значения enum
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(false);
        assertThrows(IllegalWorkExperienceCurrentBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidAccountNumber_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // пример значения enum
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER); // пример значения enum
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(false);
        assertThrows(IllegalAccountNumberBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidIsInsuranceEnabled_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(false);
        assertThrows(IllegalIsInsuranceEnabledBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_InvalidIsSalaryClient_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(true);
        when(validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(anyBoolean())).thenReturn(false);
        assertThrows(IllegalIsSalaryClientBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_ScoringEmploymentStatusRejects_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(true);
        when(validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(anyBoolean())).thenReturn(true);
        when(scoringEmploymentStatusBorrower.scoringEmploymentStatusBorrower(any())).thenReturn(false);
        assertThrows(IllegalEmploymentStatusBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_ScoringAmountCreditRejects_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(true);
        when(validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(anyBoolean())).thenReturn(true);
        when(scoringEmploymentStatusBorrower.scoringEmploymentStatusBorrower(any())).thenReturn(true);
        when(scoringAmountCredit.scoringAmountCredit(any(), any())).thenReturn(false);
        assertThrows(IllegalAmountCreditException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_ScoringBirthdateRejects_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(true);
        when(validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(anyBoolean())).thenReturn(true);
        when(scoringEmploymentStatusBorrower.scoringEmploymentStatusBorrower(any())).thenReturn(true);
        when(scoringAmountCredit.scoringAmountCredit(any(), any())).thenReturn(true);
        when(scoringBirthdateBorrower.scoringBirthdateBorrower(any())).thenReturn(false);
        assertThrows(IllegalBirthdateBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_ScoringWorkExperienceTotalRejects_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(true);
        when(validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(anyBoolean())).thenReturn(true);
        when(scoringEmploymentStatusBorrower.scoringEmploymentStatusBorrower(any())).thenReturn(true);
        when(scoringAmountCredit.scoringAmountCredit(any(), any())).thenReturn(true);
        when(scoringBirthdateBorrower.scoringBirthdateBorrower(any())).thenReturn(true);
        when(scoringWorkExperienceTotalBorrower.scoringWorkExperienceTotalBorrower(anyInt())).thenReturn(false);
        assertThrows(IllegalWorkExperienceTotalBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }

    @Test
    void calculateCredit_ScoringWorkExperienceCurrentRejects_Throws() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto validScoringData = new ScoringDataDto();
        validScoringData.setEmployment(employmentDto);
        validScoringData.setAmount(BigDecimal.valueOf(100000));
        validScoringData.setTerm(12);
        validScoringData.setFirstName("Ivan");
        validScoringData.setLastName("Ivanov");
        validScoringData.setMiddleName("Ivanovich");
        validScoringData.setGender(Gender.MALE);
        validScoringData.setBirthdate(LocalDate.of(1990, 1, 1));
        validScoringData.setPassportSeries("1234");
        validScoringData.setPassportNumber("123456");
        validScoringData.setPassportIssueDate(LocalDate.of(2010, 1, 1));
        validScoringData.setPassportIssueBranch("ОВД Москва");
        validScoringData.setMaritalStatus(MARRIED);
        validScoringData.setDependentAmount(0);
        validScoringData.setAccountNumber("12345678901234567890");
        validScoringData.setIsInsuranceEnabled(true);
        validScoringData.setIsSalaryClient(false);
        when(validateAmountCredit.validateAmountCredit(any())).thenReturn(true);
        when(validateTermCredit.validateTermCredit(anyInt())).thenReturn(true);
        when(validateFirstNameBorrower.validateFirstNameBorrower(anyString())).thenReturn(true);
        when(validateLastNameBorrower.validateLastNameBorrower(anyString())).thenReturn(true);
        when(validateMiddleNameBorrower.validateMiddleNameBorrower(anyString())).thenReturn(true);
        when(validateGenderBorrower.validateGender(any())).thenReturn(true);
        when(validateBirthdateBorrower.validateBirthdateBorrower(any())).thenReturn(true);
        when(validatePassportSeries.validatePassportSeries(anyString())).thenReturn(true);
        when(validatePassportNumber.validatePassportNumber(anyString())).thenReturn(true);
        when(validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(any())).thenReturn(true);
        when(validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(anyString())).thenReturn(true);
        when(validateMaritalStatusBorrower.validateMaritalStatusBorrower(any())).thenReturn(true);
        when(validateDependentAmountBorrower.validateDependentAmountBorrower(anyInt())).thenReturn(true);
        when(validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(any())).thenReturn(true);
        when(validateEmployerINNBorrower.validateEmployerINNBorrower(anyString())).thenReturn(true);
        when(validateSalaryBorrower.validateSalaryBorrower(any())).thenReturn(true);
        when(validatePositionBorrower.validatePositionBorrower(any())).thenReturn(true);
        when(validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(anyInt())).thenReturn(true);
        when(validateAccountNumberBorrower.validateAccountNumberBorrower(anyString())).thenReturn(true);
        when(validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(anyBoolean())).thenReturn(true);
        when(validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(anyBoolean())).thenReturn(true);
        when(scoringEmploymentStatusBorrower.scoringEmploymentStatusBorrower(any())).thenReturn(true);
        when(scoringAmountCredit.scoringAmountCredit(any(), any())).thenReturn(true);
        when(scoringBirthdateBorrower.scoringBirthdateBorrower(any())).thenReturn(true);
        when(scoringWorkExperienceTotalBorrower.scoringWorkExperienceTotalBorrower(anyInt())).thenReturn(true);
        when(scoringWorkExperienceCurrentBorrower.scoringWorkExperienceCurrentBorrower(anyInt())).thenReturn(false);
        assertThrows(IllegalWorkExperienceCurrentBorrowerException.class, () -> calculatorController.calculateCredit(validScoringData));
        verify(calculatorService, never()).calculateCredit(any());
    }


}

