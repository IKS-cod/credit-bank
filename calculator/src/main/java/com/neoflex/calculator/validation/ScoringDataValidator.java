package com.neoflex.calculator.validation;

import com.neoflex.calculator.dto.ScoringDataDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Component
public class ScoringDataValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]{2,30}$");
    private static final Pattern PASSPORT_SERIES_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern PASSPORT_NUMBER_PATTERN = Pattern.compile("^\\d{6}$");

    public boolean validate(ScoringDataDto dto) {
        if (dto == null) {
            return false;
        }

        if (!isValidName(dto.getFirstName())) {
            return false;
        }
        if (!isValidName(dto.getLastName())) {
            return false;
        }
        if (dto.getMiddleName() != null && !dto.getMiddleName().isEmpty()) {
            if (!isValidName(dto.getMiddleName())) return false;
        }
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.valueOf(20000)) < 0) return false;
        if (dto.getTerm() == null || dto.getTerm() < 6) return false;
        if (!isAdult(dto.getBirthdate())) return false;
        if (dto.getGender() == null) return false;
        if (dto.getMaritalStatus() == null) return false;
        if (dto.getPassportSeries() == null || !PASSPORT_SERIES_PATTERN.matcher(dto.getPassportSeries()).matches())
            return false;
        if (dto.getPassportNumber() == null || !PASSPORT_NUMBER_PATTERN.matcher(dto.getPassportNumber()).matches())
            return false;
        if (dto.getPassportIssueDate() == null) return false;
        if (dto.getPassportIssueBranch() == null || dto.getPassportIssueBranch().isBlank()) return false;
        if (dto.getIsInsuranceEnabled() == null) return false;
        if (dto.getIsSalaryClient() == null) return false;
        if (dto.getDependentAmount() == null) return false;
        if (dto.getAccountNumber() == null || dto.getAccountNumber().isBlank()) return false;
        if (dto.getEmployment() == null) return false;
        if (dto.getEmployment().getEmploymentStatus() == null) return false;
        if (dto.getEmployment().getEmployerINN() == null || dto.getEmployment().getEmployerINN().isBlank())
            return false;
        if (dto.getEmployment().getSalary() == null || dto.getEmployment().getSalary().compareTo(BigDecimal.ZERO) <= 0)
            return false;
        if (dto.getEmployment().getPosition() == null) return false;
        if (dto.getEmployment().getWorkExperienceTotal() == null) return false;
        if (dto.getEmployment().getWorkExperienceCurrent() == null) return false;

        return true;
    }

    private boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    private boolean isAdult(LocalDate birthdate) {
        if (birthdate == null) return false;
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthdate, today);
        return age.getYears() >= 18;
    }
}

