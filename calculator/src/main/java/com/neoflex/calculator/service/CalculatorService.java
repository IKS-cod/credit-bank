package com.neoflex.calculator.service;

import com.neoflex.calculator.dto.*;
import com.neoflex.calculator.enums.EmploymentStatus;
import com.neoflex.calculator.enums.Gender;
import com.neoflex.calculator.enums.MaritalStatus;
import com.neoflex.calculator.enums.Position;
import com.neoflex.calculator.validation.ScoringDataValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    private final ScoringDataValidator scoringDataValidator;

    @Value("${loan.baseRate}")
    private BigDecimal baseRate;

    @Value("${loan.insurancePrice}")
    private BigDecimal insurancePrice;

    private static final BigDecimal INSURANCE_RATE_DISCOUNT = BigDecimal.valueOf(3);
    private static final BigDecimal SALARY_CLIENT_RATE_DISCOUNT = BigDecimal.valueOf(1);

    public List<LoanOfferDto> calculateOffers(LoanStatementRequestDto request) {
        logger.info("Входные данные calculateOffers: {}", request);

        List<LoanOfferDto> offers = new ArrayList<>();
        boolean[] boolValues = {false, true};

        for (boolean isInsuranceEnabled : boolValues) {
            for (boolean isSalaryClient : boolValues) {
                BigDecimal rate = baseRate;
                BigDecimal totalAmount = request.getAmount();

                logger.debug("Начинаем расчёт предложения: isInsuranceEnabled={}, isSalaryClient={}", isInsuranceEnabled, isSalaryClient);

                if (isInsuranceEnabled) {
                    totalAmount = totalAmount.add(insurancePrice);
                    rate = rate.subtract(INSURANCE_RATE_DISCOUNT);
                    logger.debug("Добавлена страховка: totalAmount={}, rate={}", totalAmount, rate);
                }

                if (isSalaryClient) {
                    rate = rate.subtract(SALARY_CLIENT_RATE_DISCOUNT);
                    logger.debug("Учитывается зарплатный клиент: rate={}", rate);
                }

                BigDecimal monthlyPayment = totalAmount
                        .divide(BigDecimal.valueOf(request.getTerm()), 2, RoundingMode.HALF_UP);

                logger.debug("Рассчитан ежемесячный платёж: monthlyPayment={}", monthlyPayment);

                LoanOfferDto offer = new LoanOfferDto();
                offer.setStatementId(UUID.randomUUID());
                offer.setRequestedAmount(request.getAmount());
                offer.setTotalAmount(totalAmount);
                offer.setTerm(request.getTerm());
                offer.setMonthlyPayment(monthlyPayment);
                offer.setRate(rate.setScale(2, RoundingMode.HALF_UP));
                offer.setIsInsuranceEnabled(isInsuranceEnabled);
                offer.setIsSalaryClient(isSalaryClient);

                offers.add(offer);

                logger.debug("Добавлено предложение: {}", offer);
            }
        }

        offers.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());

        logger.info("Результат calculateOffers: {} предложений", offers.size());
        logger.info("Отсортированные предложения: {}", offers);

        return offers;
    }

    public CreditDto calculateCredit(ScoringDataDto data) {
        logger.info("Входные данные calculateCredit: {}", data);

        // Валидация базовая
        if (!scoringDataValidator.validate(data)) {
            logger.info("Прескоринг не пройден");
            throw new IllegalArgumentException("Прескоринг не пройден");
        }

        // Проверка отказов по скорингу
        if (shouldReject(data)) {
            logger.info("Отказ по скорингу");
            throw new IllegalArgumentException("Отказ по скорингу");
        }

        // Начальная ставка
        BigDecimal rate = baseRate;
        logger.debug("Начальная ставка: {}", rate);

        // Корректировка ставки по EmploymentStatus
        BigDecimal employmentAdj = getEmploymentStatusRateAdjustment(data.getEmployment().getEmploymentStatus());
        rate = rate.add(employmentAdj);
        logger.debug("Корректировка по EmploymentStatus: {}, новая ставка: {}", employmentAdj, rate);

        // Корректировка ставки по Position
        BigDecimal positionAdj = getPositionRateAdjustment(data.getEmployment().getPosition());
        rate = rate.add(positionAdj);
        logger.debug("Корректировка по Position: {}, новая ставка: {}", positionAdj, rate);

        // Корректировка ставки по MaritalStatus
        BigDecimal maritalAdj = getMaritalStatusRateAdjustment(data.getMaritalStatus());
        rate = rate.add(maritalAdj);
        logger.debug("Корректировка по MaritalStatus: {}, новая ставка: {}", maritalAdj, rate);

        // Корректировка ставки по Gender и возрасту
        BigDecimal genderAdj = getGenderRateAdjustment(data.getGender(), data.getBirthdate());
        rate = rate.add(genderAdj);
        logger.debug("Корректировка по Gender и возрасту: {}, новая ставка: {}", genderAdj, rate);

        // Учитываем страховку и зарплатного клиента
        if (Boolean.TRUE.equals(data.getIsInsuranceEnabled())) {
            rate = rate.subtract(INSURANCE_RATE_DISCOUNT);
            logger.debug("Учтена страховка, ставка уменьшена на {}: {}", INSURANCE_RATE_DISCOUNT, rate);
        }
        if (Boolean.TRUE.equals(data.getIsSalaryClient())) {
            rate = rate.subtract(SALARY_CLIENT_RATE_DISCOUNT);
            logger.debug("Учтен зарплатный клиент, ставка уменьшена на {}: {}", SALARY_CLIENT_RATE_DISCOUNT, rate);
        }

        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            rate = BigDecimal.ZERO;
            logger.debug("Ставка не может быть меньше 0, установлена в 0");
        }

        int term = data.getTerm();
        BigDecimal amount = data.getAmount();

        // Месячная процентная ставка
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);
        logger.debug("Месячная процентная ставка: {}", monthlyRate);

        // Аннуитетный платёж
        BigDecimal onePlusRPowerN = (BigDecimal.ONE.add(monthlyRate)).pow(term, MathContext.DECIMAL64);
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
        BigDecimal monthlyPayment;

        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            logger.debug("Знаменатель равен нулю, используем простое деление суммы кредита на срок");
            monthlyPayment = amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        } else {
            monthlyPayment = amount.multiply(monthlyRate).multiply(onePlusRPowerN)
                    .divide(denominator, 2, RoundingMode.HALF_UP);
            logger.debug("Рассчитан аннуитетный платёж: {}", monthlyPayment);
        }

        // Полная стоимость кредита (ПСК)
        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        BigDecimal psk = totalPayment.subtract(amount)
                .divide(amount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        logger.debug("Рассчитана ПСК: {}", psk);

        // График платежей
        List<PaymentScheduleElementDto> schedule = generatePaymentSchedule(amount, term, monthlyRate, monthlyPayment);
        logger.debug("Сформирован график платежей: {} элементов", schedule.size());

        CreditDto credit = new CreditDto();
        credit.setAmount(amount);
        credit.setTerm(term);
        credit.setMonthlyPayment(monthlyPayment);
        credit.setRate(rate.setScale(2, RoundingMode.HALF_UP));
        credit.setPsk(psk.setScale(2, RoundingMode.HALF_UP));
        credit.setIsInsuranceEnabled(data.getIsInsuranceEnabled());
        credit.setIsSalaryClient(data.getIsSalaryClient());
        credit.setPaymentSchedule(schedule);

        logger.info("Результат calculateCredit: {}", credit);

        return credit;
    }

    private boolean shouldReject(ScoringDataDto data) {
        if (data.getEmployment() == null) {
            logger.debug("Отказ: employment == null");
            return true;
        }

        EmploymentStatus status = data.getEmployment().getEmploymentStatus();
        if (status == EmploymentStatus.UNEMPLOYED) {
            logger.debug("Отказ: EmploymentStatus == UNEMPLOYED");
            return true;
        }

        int totalWorkMonths = data.getEmployment().getWorkExperienceTotal();
        int currentWorkMonths = data.getEmployment().getWorkExperienceCurrent();
        if (totalWorkMonths < 18) {
            logger.debug("Отказ: общий стаж работы меньше 18 месяцев: {}", totalWorkMonths);
            return true;
        }
        if (currentWorkMonths < 3) {
            logger.debug("Отказ: текущий стаж работы меньше 3 месяцев: {}", currentWorkMonths);
            return true;
        }

        int age = calculateAge(data.getBirthdate());
        if (age < 20 || age > 65) {
            logger.debug("Отказ: возраст вне диапазона 20-65: {}", age);
            return true;
        }

        BigDecimal salary = data.getEmployment().getSalary();
        if (salary == null) {
            logger.debug("Отказ: зарплата == null");
            return true;
        }
        BigDecimal maxAmount = salary.multiply(BigDecimal.valueOf(24));
        if (data.getAmount().compareTo(maxAmount) > 0) {
            logger.debug("Отказ: сумма кредита превышает 24-кратную зарплату. Сумма: {}, лимит: {}", data.getAmount(), maxAmount);
            return true;
        }

        return false;
    }

    private int calculateAge(LocalDate birthdate) {
        return birthdate == null ? 0 : LocalDate.now().getYear() - birthdate.getYear();
    }

    private BigDecimal getEmploymentStatusRateAdjustment(EmploymentStatus status) {
        switch (status) {
            case SELF_EMPLOYED:
                return BigDecimal.valueOf(2);
            case BUSINESS_OWNER:
                return BigDecimal.ONE;
            default:
                return BigDecimal.ZERO;
        }
    }

    private BigDecimal getPositionRateAdjustment(Position position) {
        switch (position) {
            case MIDDLE_MANAGER:
                return BigDecimal.valueOf(-2);
            case TOP_MANAGER:
                return BigDecimal.valueOf(-3);
            default:
                return BigDecimal.ZERO;
        }
    }

    private BigDecimal getMaritalStatusRateAdjustment(MaritalStatus status) {
        switch (status) {
            case MARRIED:
                return BigDecimal.valueOf(-3);
            case DIVORCED:
                return BigDecimal.ONE;
            default:
                return BigDecimal.ZERO;
        }
    }

    private BigDecimal getGenderRateAdjustment(Gender gender, LocalDate birthdate) {
        int age = calculateAge(birthdate);
        switch (gender) {
            case FEMALE:
                if (age >= 32 && age <= 60) {
                    return BigDecimal.valueOf(-3);
                }
                break;
            case MALE:
                if (age >= 30 && age <= 55) {
                    return BigDecimal.valueOf(-3);
                }
                break;
            case NON_BINARY:
                return BigDecimal.valueOf(7);
        }
        return BigDecimal.ZERO;
    }

    private List<PaymentScheduleElementDto> generatePaymentSchedule(BigDecimal principal, int term,
                                                                    BigDecimal monthlyRate, BigDecimal monthlyPayment) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal remainingDebt = principal;

        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_UP);

            PaymentScheduleElementDto element = new PaymentScheduleElementDto();
            element.setNumber(i);
            element.setDate(LocalDate.now().plusMonths(i));
            element.setTotalPayment(monthlyPayment);
            element.setInterestPayment(interestPayment);
            element.setDebtPayment(debtPayment);
            element.setRemainingDebt(remainingDebt.max(BigDecimal.ZERO));

            schedule.add(element);
        }
        return schedule;
    }
}
