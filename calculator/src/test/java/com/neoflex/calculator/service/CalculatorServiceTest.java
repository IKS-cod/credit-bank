package com.neoflex.calculator.service;

import com.neoflex.calculator.dto.*;
import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.MaritalStatus;
import com.neoflex.calculator.enums.Position;
import com.neoflex.calculator.validation.ScoringDataValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorServiceTest {

    @Mock
    private ScoringDataValidator scoringDataValidator;

    @Mock
    private Logger logger;

    @InjectMocks
    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize baseRate and insurancePrice using ReflectionTestUtils
        ReflectionTestUtils.setField(calculatorService, "baseRate", BigDecimal.valueOf(10));
        ReflectionTestUtils.setField(calculatorService, "insurancePrice", BigDecimal.valueOf(1000));
    }

    @Test
    void calculateOffers_shouldReturnOffersWithCorrectCalculations() {
        // Arrange
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        request.setAmount(BigDecimal.valueOf(10000));
        request.setTerm(12);

        // Act
        List<LoanOfferDto> offers = calculatorService.calculateOffers(request);

        // Assert
        assertEquals(4, offers.size());

        LoanOfferDto offer1 = offers.get(0);
        assertNotNull(offer1.getStatementId());
        assertEquals(request.getAmount(), offer1.getRequestedAmount());
        assertEquals(BigDecimal.valueOf(10).setScale(2), offer1.getRate());
        assertEquals(request.getAmount().divide(BigDecimal.valueOf(request.getTerm()), 2, java.math.RoundingMode.HALF_UP), offer1.getMonthlyPayment());
        assertFalse(offer1.getIsInsuranceEnabled());
        assertFalse(offer1.getIsSalaryClient());

        LoanOfferDto offer2 = offers.get(1);
        assertEquals(BigDecimal.valueOf(9).setScale(2), offer2.getRate());
        assertTrue(offer2.getIsSalaryClient());

        LoanOfferDto offer3 = offers.get(2);
        assertEquals(BigDecimal.valueOf(7).setScale(2), offer3.getRate());
        assertTrue(offer3.getIsInsuranceEnabled());
    }

    @Test
    void calculateOffers_insuranceEnabled_salaryClientEnabled_shouldApplyDiscounts() {
        // Arrange
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        request.setAmount(BigDecimal.valueOf(10000));
        request.setTerm(12);

        // Act
        List<LoanOfferDto> offers = calculatorService.calculateOffers(request);

        // Assert
        LoanOfferDto offer = offers.stream()
                .filter(o -> o.getIsInsuranceEnabled() && o.getIsSalaryClient())
                .findFirst()
                .orElse(null);

        assertNotNull(offer);
        assertEquals(BigDecimal.valueOf(6).setScale(2), offer.getRate());
        assertEquals(BigDecimal.valueOf(11000).divide(BigDecimal.valueOf(12), 2, java.math.RoundingMode.HALF_UP), offer.getMonthlyPayment());
    }
//-------------------------
@Test
void calculateCredit_shouldThrowException_whenValidationFails() {
    ScoringDataDto data = mock(ScoringDataDto.class);
    when(scoringDataValidator.validate(data)).thenReturn(false);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> calculatorService.calculateCredit(data));
    assertEquals("Прескоринг не пройден", ex.getMessage());
}

    @Test
    void calculateCredit_shouldThrowException_whenRejectConditionMet() {
        EmploymentDto employment = new EmploymentDto();
        employment.setEmploymentStatus(EmploymentStatus.UNEMPLOYED); // условие для отказа
        employment.setWorkExperienceTotal(36);
        employment.setWorkExperienceCurrent(24);
        employment.setSalary(BigDecimal.valueOf(50000));

        ScoringDataDto data = new ScoringDataDto();
        data.setEmployment(employment);
        data.setBirthdate(LocalDate.now().minusYears(35));
        data.setAmount(BigDecimal.valueOf(100000));
        data.setTerm(12);

        when(scoringDataValidator.validate(data)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> calculatorService.calculateCredit(data));
        assertEquals("Отказ по скорингу", ex.getMessage());
    }

    @Test
    void calculateCredit_shouldReturnCreditDto_whenValidData() {
        EmploymentDto employment = new EmploymentDto();
        employment.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employment.setPosition(Position.TOP_MANAGER);
        employment.setSalary(BigDecimal.valueOf(50000));
        employment.setWorkExperienceTotal(36);
        employment.setWorkExperienceCurrent(24);

        ScoringDataDto data = new ScoringDataDto();
        data.setEmployment(employment);
        data.setBirthdate(LocalDate.now().minusYears(35));
        data.setGender(Gender.FEMALE);
        data.setMaritalStatus(MaritalStatus.MARRIED);
        data.setAmount(BigDecimal.valueOf(100000));
        data.setTerm(12);
        data.setIsInsuranceEnabled(true);
        data.setIsSalaryClient(true);

        when(scoringDataValidator.validate(data)).thenReturn(true);

        CreditDto credit = calculatorService.calculateCredit(data);

        assertNotNull(credit);
        assertEquals(data.getAmount(), credit.getAmount());
        assertEquals(data.getTerm(), credit.getTerm());
        assertNotNull(credit.getPaymentSchedule());
        assertEquals(data.getTerm(), credit.getPaymentSchedule().size());
        assertTrue(credit.getRate().compareTo(BigDecimal.ZERO) >= 0);
    }
}