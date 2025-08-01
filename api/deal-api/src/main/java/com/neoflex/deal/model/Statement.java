package com.neoflex.deal.model;

import com.neoflex.deal.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statement")
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "statement_id", nullable = false, unique = true)
    private UUID statementId;

    // Внешний ключ на Client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Внешний ключ на Credit
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    // JSONB поле для applied_offer
    @Column(name = "applied_offer", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOffer appliedOffer;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    // JSONB поле для status_history
    @Column(name = "status_history", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<StatementStatusHistory> statementStatusHistory;

}

