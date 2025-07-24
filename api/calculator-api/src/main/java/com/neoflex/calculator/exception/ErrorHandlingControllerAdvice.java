package com.neoflex.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(LoanAmountLimitExceededException.class)
    public ResponseEntity<ValidationErrorResponse> handleLoanAmountLimit(LoanAmountLimitExceededException e) {
        Violation violation = new Violation("loanAmount", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ValidationErrorResponse(Collections.singletonList(violation)));
    }

    @ExceptionHandler(UnemployedApplicantException.class)
    public ResponseEntity<ValidationErrorResponse> handleUnemployedApplicant(UnemployedApplicantException e) {
        Violation violation = new Violation("employmentStatus", e.getMessage());
        ValidationErrorResponse response = new ValidationErrorResponse(Collections.singletonList(violation));
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(AgeRestrictionException.class)
    public ResponseEntity<ValidationErrorResponse> handleAgeRestriction(AgeRestrictionException e) {
        Violation violation = new Violation("birthdate", e.getMessage());
        ValidationErrorResponse response = new ValidationErrorResponse(Collections.singletonList(violation));
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(WorkExperienceException.class)
    public ResponseEntity<ValidationErrorResponse> handleWorkExperience(WorkExperienceException e) {
        Violation violation = new Violation("workExperienceTotal", e.getMessage());
        ValidationErrorResponse response = new ValidationErrorResponse(Collections.singletonList(violation));
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(WorkCurrentExperienceException.class)
    public ResponseEntity<ValidationErrorResponse> handleWorkCurrentExperience(WorkCurrentExperienceException e) {
        Violation violation = new Violation("workExperienceCurrent", e.getMessage());
        ValidationErrorResponse response = new ValidationErrorResponse(Collections.singletonList(violation));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
