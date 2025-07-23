package com.neoflex.calculator.service;

import com.neoflex.calculator.dto.*;
import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.MaritalStatus;
import com.neoflex.calculator.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();

        // Устанавливаем приватные поля с помощью ReflectionTestUtils
        ReflectionTestUtils.setField(calculatorService, "baseRate", new BigDecimal("12.0"));
        ReflectionTestUtils.setField(calculatorService, "insurancePrice", new BigDecimal("10000"));
    }

    @Test
    @DisplayName("calculateOffers возвращает 4 предложения с корректными ставками, выплатами и суммами")
    void testCalculateOffers() {
        BigDecimal baseRate = new BigDecimal("12.0");
        BigDecimal insurancePrice = new BigDecimal("10000");
        BigDecimal insuranceDiscount = new BigDecimal("3.0");
        BigDecimal salaryDiscount = new BigDecimal("1.0");

        LoanStatementRequestDto request = new LoanStatementRequestDto();
        request.setAmount(new BigDecimal("100000"));
        request.setTerm(12);

        List<LoanOfferDto> offers = calculatorService.calculateOffers(request);

        assertEquals(4, offers.size(), "Должно быть 4 варианта предложений");

        for (LoanOfferDto offer : offers) {
            // Рассчитываем ожидаемую ставку с учетом скидок
            BigDecimal expectedRate = baseRate;
            if (Boolean.TRUE.equals(offer.getIsInsuranceEnabled())) {
                expectedRate = expectedRate.subtract(insuranceDiscount);
            }
            if (Boolean.TRUE.equals(offer.getIsSalaryClient())) {
                expectedRate = expectedRate.subtract(salaryDiscount);
            }
            expectedRate = expectedRate.setScale(2, BigDecimal.ROUND_HALF_UP);
            assertEquals(0, expectedRate.compareTo(offer.getRate()), "Ставка рассчитана неверно");

            // Рассчитываем ожидаемую сумму кредита с учетом страховки
            BigDecimal totalAmount = request.getAmount();
            if (Boolean.TRUE.equals(offer.getIsInsuranceEnabled())) {
                totalAmount = totalAmount.add(insurancePrice);
            }

            int term = request.getTerm();
            BigDecimal monthlyRate = expectedRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
            BigDecimal onePlusRatePowerN = (BigDecimal.ONE.add(monthlyRate)).pow(term);

            BigDecimal annuityCoefficient = monthlyRate.multiply(onePlusRatePowerN)
                    .divide(onePlusRatePowerN.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP);

            BigDecimal expectedMonthlyPayment = totalAmount.multiply(annuityCoefficient).setScale(2, RoundingMode.HALF_UP);

            BigDecimal expectedTotalRepayment = expectedMonthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(2, RoundingMode.HALF_UP);

            // Проверяем ежемесячный платёж и общую сумму выплат
            assertEquals(0, expectedMonthlyPayment.compareTo(offer.getMonthlyPayment()), "Ежемесячный платёж рассчитан неверно");
            assertEquals(0, expectedTotalRepayment.compareTo(offer.getTotalAmount()), "Общая сумма рассчитана неверно");

            assertEquals(request.getAmount(), offer.getRequestedAmount(), "Сумма запроса не совпадает");
            assertEquals(term, offer.getTerm(), "Срок кредита не совпадает");
            assertNotNull(offer.getStatementId(), "Идентификатор заявки не должен быть null");
            assertTrue(offer.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0, "Ежемесячный платёж должен быть положительным");
        }
    }


    @Test
    @DisplayName("calculateOffers возвращает предложения, отсортированные по убыванию ставки")
    void testOffersSortedByRateDescending() {
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        request.setAmount(new BigDecimal("50000"));
        request.setTerm(24);

        List<LoanOfferDto> offers = calculatorService.calculateOffers(request);

        for (int i = 0; i < offers.size() - 1; i++) {
            BigDecimal currentRate = offers.get(i).getRate();
            BigDecimal nextRate = offers.get(i + 1).getRate();
            assertTrue(currentRate.compareTo(nextRate) >= 0, "Предложения не отсортированы по убыванию ставки");
        }
    }


    @Test
    @DisplayName("calculateCredit корректно рассчитывает кредит с базовыми данными")
    void testCalculateCreditBasic() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto scoringData = new ScoringDataDto();
        scoringData.setAmount(new BigDecimal("100000"));
        scoringData.setTerm(12);
        scoringData.setIsInsuranceEnabled(false);
        scoringData.setIsSalaryClient(false);
        scoringData.setBirthdate(LocalDate.now().minusYears(35));
        scoringData.setGender(Gender.MALE);
        scoringData.setMaritalStatus(MaritalStatus.MARRIED);
        scoringData.setEmployment(employmentDto);

        CreditDto credit = calculatorService.calculateCredit(scoringData);

        assertNotNull(credit);
        assertEquals(scoringData.getAmount(), credit.getAmount());
        assertEquals(scoringData.getTerm(), credit.getTerm());
        assertTrue(credit.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(credit.getRate().compareTo(BigDecimal.ZERO) >= 0);
        assertEquals(scoringData.getIsInsuranceEnabled(), credit.getIsInsuranceEnabled());
        assertEquals(scoringData.getIsSalaryClient(), credit.getIsSalaryClient());

        // Проверяем, что график платежей заполнен и содержит нужное число элементов
        List<PaymentScheduleElementDto> schedule = credit.getPaymentSchedule();
        assertNotNull(schedule);
        assertEquals(scoringData.getTerm(), schedule.size());

        // Проверяем, что сумма всех платежей равна ПСК (приблизительно)
        BigDecimal sumPayments = schedule.stream()
                .map(PaymentScheduleElementDto::getTotalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        assertEquals(credit.getPsk().setScale(2, BigDecimal.ROUND_HALF_UP), sumPayments);
    }

    @Test
    @DisplayName("calculateCredit корректно учитывает скидки по страховке и зарплатному клиенту")
    void testCalculateCreditWithDiscounts() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(BigDecimal.valueOf(75000.00));
        employmentDto.setPosition(Position.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);
        ScoringDataDto scoringData = new ScoringDataDto();
        scoringData.setAmount(new BigDecimal("100000"));
        scoringData.setTerm(12);
        scoringData.setIsInsuranceEnabled(true);
        scoringData.setIsSalaryClient(true);
        scoringData.setBirthdate(LocalDate.now().minusYears(40));
        scoringData.setGender(Gender.FEMALE);
        scoringData.setMaritalStatus(MaritalStatus.MARRIED);
        scoringData.setEmployment(employmentDto);

        CreditDto credit = calculatorService.calculateCredit(scoringData);

        assertNotNull(credit);
        // Ожидаем, что ставка будет меньше базовой из-за скидок и корректировок
        assertTrue(credit.getRate().compareTo(new BigDecimal("0")) >= 0);
        assertTrue(credit.getRate().compareTo(new BigDecimal("12")) < 0);

        // Проверяем, что скидки применены (ставка уменьшена)
        BigDecimal expectedRate = new BigDecimal("12.0")
                .add(new BigDecimal("2"))    // SELF_EMPLOYED +2
                .add(new BigDecimal("-2"))   // MIDDLE_MANAGER -2
                .add(new BigDecimal("-3"))   // MARRIED -3
                .add(new BigDecimal("-3"))   // FEMALE 32-60 -3
                .subtract(new BigDecimal("3"))  // страховка -3
                .subtract(new BigDecimal("1")); // зарплатный клиент -1

        if (expectedRate.compareTo(BigDecimal.ZERO) < 0) {
            expectedRate = BigDecimal.ZERO;
        }

        assertEquals(0, expectedRate.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(credit.getRate()));

        // Проверяем, что ежемесячный платёж положительный
        assertTrue(credit.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0);

        // Проверяем график платежей
        assertNotNull(credit.getPaymentSchedule());
        assertEquals(scoringData.getTerm(), credit.getPaymentSchedule().size());
    }
}

