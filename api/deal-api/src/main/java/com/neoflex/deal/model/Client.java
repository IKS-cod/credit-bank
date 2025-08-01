package com.neoflex.deal.model;

import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id", nullable = false, unique = true)
    private UUID clientId;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "passport", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Passport passport;

    @Column(name = "employment", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Employment employment;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

}
