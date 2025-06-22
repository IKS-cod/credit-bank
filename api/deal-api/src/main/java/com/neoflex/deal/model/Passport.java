package com.neoflex.deal.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Passport {

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

}



