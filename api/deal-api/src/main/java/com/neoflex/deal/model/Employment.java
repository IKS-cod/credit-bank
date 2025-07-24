package com.neoflex.deal.model;

import com.neoflex.deal.enums.EmploymentPosition;
import com.neoflex.deal.enums.EmploymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Employment {

    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

    @Override
    public String toString() {
        return "Employment{" +
                "status=" + status +
                ", employerInn='" + employerInn + '\'' +
                ", salary=" + salary +
                ", position=" + position +
                ", workExperienceTotal=" + workExperienceTotal +
                ", workExperienceCurrent=" + workExperienceCurrent +
                '}';
    }
}


