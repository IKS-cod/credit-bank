package com.neoflex.calculator.controller;

import com.neoflex.calculator.dto.CreditDto;
import com.neoflex.calculator.dto.LoanOfferDto;
import com.neoflex.calculator.dto.LoanStatementRequestDto;
import com.neoflex.calculator.dto.ScoringDataDto;
import com.neoflex.calculator.exception.*;
import com.neoflex.calculator.service.CalculatorService;
import com.neoflex.calculator.validation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Tag(name = "Calculator API", description = "Методы для расчёта кредитных предложений и параметров кредита")
public class CalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private final CalculatorService calculatorService;
    private final ValidatePassportSeries validatePassportSeries;
    private final ValidatePassportNumber validatePassportNumber;
    private final ValidateEmailBorrower validateEmailBorrower;
    private final ValidateBirthdateBorrower validateBirthdateBorrower;
    private final ValidateTermCredit validateTermCredit;
    private final ValidateAmountCredit validateAmountCredit;
    private final ValidateMiddleNameBorrower validateMiddleNameBorrower;
    private final ValidateLastNameBorrower validateLastNameBorrower;
    private final ValidateFirstNameBorrower validateFirstNameBorrower;
    private final ValidateGenderBorrower validateGenderBorrower;
    private final ValidateIsInsuranceEnabledBorrower validateIsInsuranceEnabledBorrower;
    private final ValidateIsSalaryClientBorrower validateIsSalaryClientBorrower;
    private final ValidateMaritalStatusBorrower validateMaritalStatusBorrower;
    private final ValidatePassportIssueDateBorrower validatePassportIssueDateBorrower;
    private final ValidatePassportIssueBranchBorrower validatePassportIssueBranchBorrower;
    private final ValidateDependentAmountBorrower validateDependentAmountBorrower;
    private final ValidateAccountNumberBorrower validateAccountNumberBorrower;
    private final ValidateEmploymentStatusBorrower validateEmploymentStatusBorrower;
    private final ValidateEmployerINNBorrower validateEmployerINNBorrower;
    private final ValidateSalaryBorrower validateSalaryBorrower;
    private final ValidatePositionBorrower validatePositionBorrower;
    private final ValidateWorkExperienceTotalBorrower validateWorkExperienceTotalBorrower;
    private final ValidateWorkExperienceCurrentBorrower validateWorkExperienceCurrentBorrower;
    private final ScoringEmploymentStatusBorrower scoringEmploymentStatusBorrower;
    private final ScoringAmountCredit scoringAmountCredit;
    private final ScoringBirthdateBorrower scoringBirthdateBorrower;
    private final ScoringWorkExperienceTotalBorrower scoringWorkExperienceTotalBorrower;
    private final ScoringWorkExperienceCurrentBorrower scoringWorkExperienceCurrentBorrower;

    @PostMapping("/offers")
    @Operation(summary = "Рассчитать кредитные предложения",
            description = "Принимает данные заявки и возвращает список возможных кредитных предложений")
    public List<LoanOfferDto> calculateOffers(@RequestBody LoanStatementRequestDto request) {
        logger.info("Endpoint /offers - calculateOffers: Входные данные для calculateOffers: {}", request);
        if (!validateFirstNameBorrower.validateFirstNameBorrower(request.getFirstName())) {
            logger.info("Endpoint /offers - calculateOffers:Ошибка Имя заемщика должно быть 2-30 латинских букв, {}", request.getFirstName());
            throw new IllegalFirstNameBorrowerException("Endpoint /offers - calculateOffers:Ошибка Имя заемщика должно быть 2-30 латинских букв");
        }
        if (!validateLastNameBorrower.validateLastNameBorrower(request.getLastName())) {
            logger.info("Endpoint /offers - calculateOffers:Ошибка Фамилия должна быть 2-30 латинских букв, {}", request.getLastName());
            throw new IllegalLastNameBorrowerException("Endpoint /offers - calculateOffers:Ошибка Фамилия заемщика должна быть 2-30 латинских букв");
        }
        if (!validateMiddleNameBorrower.validateMiddleNameBorrower(request.getMiddleName())) {
            logger.info("Endpoint /offers - calculateOffers:Ошибка Отчество должно быть 2-30 латинских букв, {}", request.getMiddleName());
            throw new IllegalMiddleNameBorrowerException("Endpoint /offers - calculateOffers:Ошибка Отчество заемщика должно быть 2-30 латинских букв");
        }
        if (!validateAmountCredit.validateAmountCredit(request.getAmount())) {
            logger.info("Endpoint /offers - calculateOffers:Ошибка Суммы: минимальное значение 20000, {}", request.getAmount());
            throw new IllegalAmountCreditException("Endpoint /offers - calculateOffers:Ошибка Суммы: минимальное значение 20000");
        }
        if (!validateTermCredit.validateTermCredit(request.getTerm())) {
            logger.info("Endpoint /offers - calculateOffers:Ошибка Срока: минимальное значение 6 месяцев, {}", request.getTerm());
            throw new IllegalTermCreditException("Endpoint /offers - calculateOffers:Ошибка Срока: минимальное значение 6 месяцев");
        }
        if (!validateBirthdateBorrower.validateBirthdateBorrower(request.getBirthdate())) {
            logger.info("Endpoint /offers - calculateOffers:Ошибка Даты рождения: возраст должен быть ≥18 лет, {}", request.getBirthdate());
            throw new IllegalBirthdateBorrowerException("Endpoint /offers - calculateOffers:Ошибка Даты рождения: возраст должен быть ≥18 лет");
        }
        if (!validateEmailBorrower.validateEmailBorrower(request.getEmail())) {
            logger.info("Endpoint /offers - calculateOffers:Ошибка Email: неверный формат, {}", request.getEmail());
            throw new IllegalEmailBorrowerException("Endpoint /offers - calculateOffers:Ошибка Email: неверный формат");
        }
        if (!validatePassportNumber.validatePassportNumber(request.getPassportNumber())) {
            logger.info("Endpoint /offers - calculateOffers:Неверный Номер паспорта: {}", request.getPassportNumber());
            throw new IllegalPassportNumberException("Endpoint /offers - calculateOffers:Номер паспорта должен содержать 6 цифр");
        }
        if (!validatePassportSeries.validatePassportSeries(request.getPassportSeries())) {
            logger.info("Endpoint /offers - calculateOffers:Неверная Серия паспорта: {}", request.getPassportSeries());
            throw new IllegalPassportSeriesException("Endpoint /offers - calculateOffers:Серия паспорта должна содержать 4 цифры");
        }
        List<LoanOfferDto> offers = calculatorService.calculateOffers(request);
        logger.info("Endpoint /offers - calculateOffers:Результат calculateOffers: {}", offers);
        return offers;
    }

    @PostMapping("/calc")
    @Operation(summary = "Рассчитать параметры кредита",
            description = "Выполняет скоринг и рассчитывает параметры кредита по данным клиента")
    public CreditDto calculateCredit(@RequestBody ScoringDataDto scoringData) {
        logger.info("Endpoint /calc - calculateCredit:Входные данные для calculateCredit: {}", scoringData);

        if (!validateAmountCredit.validateAmountCredit(scoringData.getAmount())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка Суммы: минимальное значение 20000, {}", scoringData.getAmount());
            throw new IllegalAmountCreditException("Endpoint /calc - calculateCredit:Ошибка Суммы: минимальное значение 20000");
        }
        if (!validateTermCredit.validateTermCredit(scoringData.getTerm())) {
            logger.info("Endpoint /calc - calculateCredit:Ошибка Срока: минимальное значение 6 месяцев, {}", scoringData.getTerm());
            throw new IllegalTermCreditException("Endpoint /calc - calculateCredit:Ошибка Срока: минимальное значение 6 месяцев");
        }
        if (!validateFirstNameBorrower.validateFirstNameBorrower(scoringData.getFirstName())) {
            logger.info("Endpoint /calc - calculateCredit:Ошибка Имя заемщика должно быть 2-30 латинских букв, {}", scoringData.getFirstName());
            throw new IllegalFirstNameBorrowerException("Endpoint /calc - calculateCredit:Ошибка Имя заемщика должно быть 2-30 латинских букв");
        }
        if (!validateLastNameBorrower.validateLastNameBorrower(scoringData.getLastName())) {
            logger.info("Endpoint /calc - calculateCredit:Ошибка Фамилия должна быть 2-30 латинских букв, {}", scoringData.getLastName());
            throw new IllegalLastNameBorrowerException("Endpoint /calc - calculateCredit:Ошибка Фамилия заемщика должна быть 2-30 латинских букв");
        }
        if (!validateMiddleNameBorrower.validateMiddleNameBorrower(scoringData.getMiddleName())) {
            logger.info("Endpoint /calc - calculateCredit:Ошибка Отчество должно быть 2-30 латинских букв, {}", scoringData.getMiddleName());
            throw new IllegalMiddleNameBorrowerException("Endpoint /calc - calculateCredit:Ошибка Отчество заемщика должно быть 2-30 латинских букв");
        }
        if (!validateGenderBorrower.validateGender(scoringData.getGender())) {
            logger.info("Endpoint /calc - calculateCredit:Ошибка Пол заемщика должен соответствовать enum Gender, {}", scoringData.getGender());
            throw new IllegalGenderBorrowerException("Endpoint /calc - calculateCredit:Ошибка нужно указать Пол заемщика");
        }
        if (!validateBirthdateBorrower.validateBirthdateBorrower(scoringData.getBirthdate())) {
            logger.info("Endpoint /calc - calculateCredit:Ошибка Даты рождения: возраст должен быть ≥18 лет, {}", scoringData.getBirthdate());
            throw new IllegalBirthdateBorrowerException("Endpoint /calc - calculateCredit:Ошибка Даты рождения: возраст должен быть ≥18 лет");
        }
        if (!validatePassportSeries.validatePassportSeries(scoringData.getPassportSeries())) {
            logger.info("Endpoint /calc - calculateCredit:Неверная Серия паспорта: {}", scoringData.getPassportSeries());
            throw new IllegalPassportSeriesException("Endpoint /calc - calculateCredit:Серия паспорта должна содержать 4 цифры");
        }
        if (!validatePassportNumber.validatePassportNumber(scoringData.getPassportNumber())) {
            logger.info("Endpoint /calc - calculateCredit:Неверный Номер паспорта: {}", scoringData.getPassportNumber());
            throw new IllegalPassportNumberException("Endpoint /calc - calculateCredit:Номер паспорта должен содержать 6 цифр");
        }
        if (!validatePassportIssueDateBorrower.validatePassportIssueDateBorrower(scoringData.getPassportIssueDate())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка даты выдачи паспорта: {}", scoringData.getPassportIssueDate());
            throw new IllegalPassportIssueDateBorrowerException("Endpoint /calc - calculateCredit: Дата выдачи паспорта обязательна и не может быть в будущем");
        }
        if (!validatePassportIssueBranchBorrower.validatePassportIssueBranchBorrower(scoringData.getPassportIssueBranch())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'Кем выдан паспорт': {}", scoringData.getPassportIssueBranch());
            throw new IllegalPassportIssueBranchBorrowerException("Endpoint /calc - calculateCredit: Поле 'Кем выдан паспорт' обязательно для заполнения");
        }
        if (!validateMaritalStatusBorrower.validateMaritalStatusBorrower(scoringData.getMaritalStatus())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка Семейное положение должно соответствовать enum MaritalStatus, {}", scoringData.getMaritalStatus());
            throw new IllegalMaritalStatusException("Endpoint /calc - calculateCredit: Ошибка нужно указать Семейное положение");
        }
        if (!validateDependentAmountBorrower.validateDependentAmountBorrower(scoringData.getDependentAmount())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'Количество иждивенцев': {}", scoringData.getDependentAmount());
            throw new IllegalDependentAmountBorrowerException("Endpoint /calc - calculateCredit: Поле 'Количество иждивенцев' должно быть от 0 до 20");
        }
        if (!validateEmploymentStatusBorrower.validateEmploymentStatusBorrower(scoringData.getEmployment().getEmploymentStatus())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'Статус занятости': {}", scoringData.getEmployment().getEmploymentStatus());
            throw new IllegalEmploymentStatusBorrowerException("Endpoint /calc - calculateCredit: Поле 'Статус занятости' обязательно для заполнения");
        }

        if (!validateEmployerINNBorrower.validateEmployerINNBorrower(scoringData.getEmployment().getEmployerINN())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'ИНН работодателя': {}", scoringData.getEmployment().getEmployerINN());
            throw new IllegalEmployerINNBorrowerException("Endpoint /calc - calculateCredit: Поле 'ИНН работодателя' обязательно и должно содержать 10 или 12 цифр");
        }
        if (!validateSalaryBorrower.validateSalaryBorrower(scoringData.getEmployment().getSalary())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'Заработная плата': {}", scoringData.getEmployment().getSalary());
            throw new IllegalSalaryBorrowerException("Endpoint /calc - calculateCredit: Заработная плата обязательна и должна быть положительным числом");
        }
        if (!validatePositionBorrower.validatePositionBorrower(scoringData.getEmployment().getPosition())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'Должность': {}", scoringData.getEmployment().getPosition());
            throw new IllegalPositionBorrowerException("Endpoint /calc - calculateCredit: Поле 'Должность' обязательно для заполнения");
        }
        if (!validateWorkExperienceTotalBorrower.validateWorkExperienceTotalBorrower(scoringData.getEmployment().getWorkExperienceTotal())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'Общий трудовой стаж': {}", scoringData.getEmployment().getWorkExperienceTotal());
            throw new IllegalWorkExperienceTotalBorrowerException("Endpoint /calc - calculateCredit: Общий трудовой стаж обязателен и не может быть отрицательным");
        }
        if (!validateWorkExperienceCurrentBorrower.validateWorkExperienceCurrentBorrower(scoringData.getEmployment().getWorkExperienceCurrent())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка поля 'Текущий трудовой стаж': {}", scoringData.getEmployment().getWorkExperienceCurrent());
            throw new IllegalWorkExperienceCurrentBorrowerException("Endpoint /calc - calculateCredit: Текущий трудовой стаж обязателен и не может быть отрицательным");
        }
        if (!validateAccountNumberBorrower.validateAccountNumberBorrower(scoringData.getAccountNumber())) {
            logger.info("Endpoint /calc - calculateCredit: Ошибка номера банковского счета: {}", scoringData.getAccountNumber());
            throw new IllegalAccountNumberBorrowerException("Endpoint /calc - calculateCredit: Номер банковского счета должен состоять из 20 цифр");
        }
        if (!validateIsInsuranceEnabledBorrower.validateIsInsuranceEnabledBorrower(scoringData.getIsInsuranceEnabled())) {
            logger.info("Endpoint /calc - calculateCredit: Поле isInsuranceEnabled обязательно для заполнения");
            throw new IllegalIsInsuranceEnabledBorrowerException("Endpoint /calc - calculateCredit: Поле isInsuranceEnabled обязательно для заполнения");
        }
        if (!validateIsSalaryClientBorrower.validateIsSalaryClientBorrower(scoringData.getIsSalaryClient())) {
            logger.info("Endpoint /calc - calculateCredit: Поле isSalaryClient обязательно для заполнения");
            throw new IllegalIsSalaryClientBorrowerException("Endpoint /calc - calculateCredit: Поле isSalaryClient обязательно для заполнения");
        }
        if (!scoringEmploymentStatusBorrower.scoringEmploymentStatusBorrower(scoringData.getEmployment().getEmploymentStatus())) {
            logger.info("Endpoint /calc - calculateCredit: Отказ в кредите из-за 'Статус занятости': {}", scoringData.getEmployment().getEmploymentStatus());
            throw new IllegalEmploymentStatusBorrowerException("Endpoint /calc - calculateCredit: Отказ в кредите: безработным кредит не даем");
        }
        if (!scoringAmountCredit.scoringAmountCredit(scoringData.getAmount(), scoringData.getEmployment().getSalary())) {
            logger.info("Endpoint /calc - calculateCredit: Отказ в кредите: сумма кредита больше 24 зарплат: сумма - {}, зарплата - {}", scoringData.getAmount(), scoringData.getEmployment().getSalary());
            throw new IllegalAmountCreditException("Endpoint /calc - calculateCredit: Отказ в кредите: сумма кредита больше 24 зарплат");
        }

        if (!scoringBirthdateBorrower.scoringBirthdateBorrower(scoringData.getBirthdate())) {
            logger.info("Endpoint /calc - calculateCredit:Отказ в кредите: возраст должен быть больше 20 и меньше 65 лет, {}", scoringData.getBirthdate());
            throw new IllegalBirthdateBorrowerException("Endpoint /calc - calculateCredit:Отказ в кредите: возраст должен быть больше 20 и меньше 65 лет");
        }
        if (!scoringWorkExperienceTotalBorrower.scoringWorkExperienceTotalBorrower(scoringData.getEmployment().getWorkExperienceTotal())) {
            logger.info("Endpoint /calc - calculateCredit: Отказ в кредите: Общий стаж менее 18 месяцев: {}", scoringData.getEmployment().getWorkExperienceTotal());
            throw new IllegalWorkExperienceTotalBorrowerException("Endpoint /calc - calculateCredit: Отказ в кредите: Общий стаж менее 18 месяцев");
        }
        if (!scoringWorkExperienceCurrentBorrower.scoringWorkExperienceCurrentBorrower(scoringData.getEmployment().getWorkExperienceCurrent())) {
            logger.info("Endpoint /calc - calculateCredit: Отказ в кредите: Текущий стаж менее 3 месяцев: {}", scoringData.getEmployment().getWorkExperienceCurrent());
            throw new IllegalWorkExperienceCurrentBorrowerException("Endpoint /calc - calculateCredit: Отказ в кредите: Текущий стаж менее 3 месяцев");
        }


        CreditDto credit = calculatorService.calculateCredit(scoringData);
        logger.info("Endpoint /calc - calculateCredit:Результат calculateCredit: {}", credit);
        return credit;
    }
}
