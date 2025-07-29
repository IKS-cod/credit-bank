package com.neoflex.deal.model;

import com.neoflex.deal.enums.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementStatusHistory {

    private String status;
    private LocalDateTime time;
    private ChangeType changeType;
}


