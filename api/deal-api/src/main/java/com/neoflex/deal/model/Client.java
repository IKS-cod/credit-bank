package com.neoflex.deal.model;

import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;
import java.time.LocalDate;

@Setter
@Getter
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

    @Column(name = "email", unique = true)
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

    public Client() {
    }

    public Client(UUID clientId, String lastName, String firstName, String middleName, LocalDate birthDate,
                  String email, Gender gender, MaritalStatus maritalStatus, Integer dependentAmount,
                  Passport passport, Employment employment, String accountNumber) {
        this.clientId = clientId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.email = email;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.dependentAmount = dependentAmount;
        this.passport = passport;
        this.employment = employment;
        this.accountNumber = accountNumber;
    }

}
