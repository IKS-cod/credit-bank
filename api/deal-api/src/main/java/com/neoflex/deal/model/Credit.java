package com.neoflex.deal.model;

import com.neoflex.deal.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credit")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_id", nullable = false, unique = true)
    private UUID creditId;

    @Column(name = "amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal amount;

    @Column(name = "term", nullable = false)
    private Integer term;

    @Column(name = "monthly_payment", precision = 19, scale = 4)
    private BigDecimal monthlyPayment;

    @Column(name = "rate", precision = 19, scale = 4)
    private BigDecimal rate;

    @Column(name = "psk", precision = 19, scale = 4)
    private BigDecimal psk;

    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<PaymentSchedule> paymentSchedule;

    @Column(name = "insurance_enabled")
    private Boolean insuranceEnabled;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    private CreditStatus creditStatus;
}

