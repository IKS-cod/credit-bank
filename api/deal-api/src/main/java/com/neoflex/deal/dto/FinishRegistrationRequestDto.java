package com.neoflex.deal.dto;

import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.enums.MaritalStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FinishRegistrationRequestDto {

    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDto employment;
    private String accountNumber;

}

