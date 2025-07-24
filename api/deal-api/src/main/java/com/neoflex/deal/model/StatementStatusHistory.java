package com.neoflex.deal.model;

import com.neoflex.deal.enums.ChangeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class StatementStatusHistory {

    private String status;
    private LocalDateTime time;
    private ChangeType changeType;

    public StatementStatusHistory() {
    }

    public StatementStatusHistory(String status, LocalDateTime time, ChangeType changeType) {
        this.status = status;
        this.time = time;
        this.changeType = changeType;
    }

    @Override
    public String toString() {
        return "StatementStatusHistory{" +
                "status='" + status + '\'' +
                ", time=" + time +
                ", changeType=" + changeType +
                '}';
    }
}


