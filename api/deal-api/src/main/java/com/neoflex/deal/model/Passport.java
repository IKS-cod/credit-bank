package com.neoflex.deal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passport {

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}



