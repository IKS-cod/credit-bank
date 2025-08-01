package com.neoflex.deal.mapper;

import com.neoflex.deal.dto.*;
import com.neoflex.deal.enums.CreditStatus;
import com.neoflex.deal.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DealMapper {

    public Client toClient(LoanStatementRequestDto dto) {
        Client client = new Client();
        client.setClientId(null);
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setMiddleName(dto.getMiddleName());
        client.setEmail(dto.getEmail());
        client.setBirthDate(dto.getBirthdate());

        Passport passport = new Passport();
        passport.setNumber(dto.getPassportNumber());
        passport.setSeries(dto.getPassportSeries());
        client.setPassport(passport);

        return client;
    }

    public LoanOffer toLoanOffer(LoanOfferDto dto) {
        LoanOffer loanOffer = new LoanOffer();
        loanOffer.setStatementId(dto.getStatementId());
        loanOffer.setRequestedAmount(dto.getRequestedAmount());
        loanOffer.setTotalAmount(dto.getTotalAmount());
        loanOffer.setTerm(dto.getTerm());
        loanOffer.setMonthlyPayment(dto.getMonthlyPayment());
        loanOffer.setRate(dto.getRate());
        loanOffer.setIsInsuranceEnabled(dto.getIsInsuranceEnabled());
        loanOffer.setIsSalaryClient(dto.getIsSalaryClient());
        return loanOffer;
    }

    public ScoringDataDto toScoringDataDto(FinishRegistrationRequestDto finishDto, Client client, LoanOffer offer) {
        ScoringDataDto scoringData = new ScoringDataDto();
        scoringData.setFirstName(client.getFirstName());
        scoringData.setLastName(client.getLastName());
        scoringData.setMiddleName(client.getMiddleName());
        scoringData.setGender(finishDto.getGender());
        scoringData.setBirthdate(client.getBirthDate());
        scoringData.setPassportSeries(client.getPassport().getSeries());
        scoringData.setPassportNumber(client.getPassport().getNumber());
        scoringData.setPassportIssueDate(finishDto.getPassportIssueDate());
        scoringData.setPassportIssueBranch(finishDto.getPassportIssueBranch());
        scoringData.setMaritalStatus(finishDto.getMaritalStatus());
        scoringData.setDependentAmount(finishDto.getDependentAmount());
        scoringData.setEmployment(finishDto.getEmployment());
        scoringData.setAccountNumber(finishDto.getAccountNumber());

        if (offer != null) {
            scoringData.setAmount(offer.getRequestedAmount());
            scoringData.setTerm(offer.getTerm());
            scoringData.setIsInsuranceEnabled(offer.getIsInsuranceEnabled());
            scoringData.setIsSalaryClient(offer.getIsSalaryClient());
        }

        return scoringData;
    }

    public Credit toCredit(Credit credit, CreditDto creditDto) {
        credit.setAmount(creditDto.getAmount());
        credit.setTerm(creditDto.getTerm());
        credit.setMonthlyPayment(creditDto.getMonthlyPayment());
        credit.setRate(creditDto.getRate());
        credit.setPsk(creditDto.getPsk());
        List<PaymentSchedule> paymentSchedule = creditDto.getPaymentSchedule().stream()
                .map(dto -> {
                    PaymentSchedule element = new PaymentSchedule();
                    element.setNumber(dto.getNumber());
                    element.setDate(dto.getDate());
                    element.setTotalPayment(dto.getTotalPayment());
                    element.setInterestPayment(dto.getInterestPayment());
                    element.setDebtPayment(dto.getDebtPayment());
                    element.setRemainingDebt(dto.getRemainingDebt());
                    return element;
                })
                .collect(Collectors.toList());

        credit.setPaymentSchedule(paymentSchedule);
        credit.setInsuranceEnabled(creditDto.getIsInsuranceEnabled());
        credit.setSalaryClient(creditDto.getIsSalaryClient());
        credit.setCreditStatus(CreditStatus.CALCULATED);

        return credit;
    }

    public Employment toEmployment(FinishRegistrationRequestDto finishDto) {
        Employment employment = new Employment();
        employment.setStatus(finishDto.getEmployment().getEmploymentStatus());
        employment.setEmployerInn(finishDto.getEmployment().getEmployerINN());
        employment.setSalary(finishDto.getEmployment().getSalary());
        employment.setPosition(finishDto.getEmployment().getPosition());
        employment.setWorkExperienceTotal(finishDto.getEmployment().getWorkExperienceTotal());
        employment.setWorkExperienceCurrent(finishDto.getEmployment().getWorkExperienceCurrent());


        return employment;
    }
}
