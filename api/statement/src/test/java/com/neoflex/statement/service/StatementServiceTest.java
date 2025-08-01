package com.neoflex.statement.service;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.integration.DealRequester;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

    @Mock
    private DealRequester dealRequester;

    @InjectMocks
    private StatementService statementService;

    @Test
    void getOffers_ShouldReturnListOfLoanOfferDto() {
        // given
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        requestDto.setAmount(new BigDecimal("50000.00"));
        requestDto.setTerm(12);
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setMiddleName("Michael");
        requestDto.setEmail("john.doe@example.com");
        requestDto.setBirthdate(LocalDate.of(1990, 5, 15));
        requestDto.setPassportSeries("1234");
        requestDto.setPassportNumber("567890");

        List<LoanOfferDto> mockOffers = List.of(
                new LoanOfferDto() {{
                    setStatementId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
                    setRequestedAmount(new BigDecimal("50000.00"));
                    setTotalAmount(new BigDecimal("55000.00"));
                    setTerm(12);
                    setMonthlyPayment(new BigDecimal("4583.33"));
                    setRate(new BigDecimal("12.5"));
                    setIsInsuranceEnabled(true);
                    setIsSalaryClient(false);
                }},
                new LoanOfferDto() {{
                    setStatementId(UUID.fromString("321e6547-e89b-12d3-a456-426614174999"));
                    setRequestedAmount(new BigDecimal("75000.00"));
                    setTotalAmount(new BigDecimal("84000.00"));
                    setTerm(24);
                    setMonthlyPayment(new BigDecimal("3500.00"));
                    setRate(new BigDecimal("11.0"));
                    setIsInsuranceEnabled(false);
                    setIsSalaryClient(true);
                }}
        );

        when(dealRequester.getLoanOffers(requestDto)).thenReturn(mockOffers);

        // when
        List<LoanOfferDto> offers = statementService.getOffers(requestDto);

        // then
        verify(dealRequester).getLoanOffers(requestDto);
        assertThat(offers).usingRecursiveComparison().isEqualTo(mockOffers);
    }

    @Test
    void selectOffer_ShouldCallDealRequesterWithCorrectArgument() {
        // given
        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(new BigDecimal("50000.00"));
        offerDto.setTerm(12);
        offerDto.setTotalAmount(new BigDecimal("55000.00"));
        offerDto.setMonthlyPayment(new BigDecimal("4583.33"));
        offerDto.setRate(new BigDecimal("12.5"));
        offerDto.setIsInsuranceEnabled(true);
        offerDto.setIsSalaryClient(false);

        // when
        statementService.selectOffer(offerDto);

        // then
        ArgumentCaptor<LoanOfferDto> captor = ArgumentCaptor.forClass(LoanOfferDto.class);
        verify(dealRequester).selectLoanOffer(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(offerDto);
    }
}
