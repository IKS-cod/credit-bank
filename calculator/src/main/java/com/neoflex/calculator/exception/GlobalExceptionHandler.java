package com.neoflex.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Глобальный обработчик исключений, обрабатывающий различные типы ошибок и возвращающий соответствующие HTTP-статусы.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик исключений, связанных с неверными данными или нарушением бизнес-логики (400 BAD REQUEST).
     *
     * @param ex исключение, содержащее сообщение об ошибке
     * @return ответ с сообщением об ошибке и статусом 400
     */
    @ExceptionHandler({
            IllegalAmountCreditException.class,
            IllegalBirthdateBorrowerException.class,
            IllegalEmailBorrowerException.class,
            IllegalFirstNameBorrowerException.class,
            IllegalLastNameBorrowerException.class,
            IllegalMiddleNameBorrowerException.class,
            IllegalPassportNumberException.class,
            IllegalPassportSeriesException.class,
            IllegalTermCreditException.class,
            IllegalGenderBorrowerException.class,
            IllegalIsInsuranceEnabledBorrowerException.class,
            IllegalIsSalaryClientBorrowerException.class,
            IllegalMaritalStatusException.class,
            IllegalPassportIssueDateBorrowerException.class,
            IllegalPassportIssueBranchBorrowerException.class,
            IllegalDependentAmountBorrowerException.class,
            IllegalAccountNumberBorrowerException.class,
            IllegalEmploymentStatusBorrowerException.class,
            IllegalEmployerINNBorrowerException.class,
            IllegalSalaryBorrowerException.class,
            IllegalPositionBorrowerException.class,
            IllegalWorkExperienceTotalBorrowerException.class,
            IllegalWorkExperienceCurrentBorrowerException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequestException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
