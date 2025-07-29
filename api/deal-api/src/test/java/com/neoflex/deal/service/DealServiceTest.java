package com.neoflex.deal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.deal.dto.*;
import com.neoflex.deal.enums.ApplicationStatus;
import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.exception.ClientNotFoundException;
import com.neoflex.deal.exception.StatementNotFoundException;
import com.neoflex.deal.integration.CalculatorRequester;
import com.neoflex.deal.mapper.DealMapper;
import com.neoflex.deal.model.*;
import com.neoflex.deal.repository.ClientRepository;
import com.neoflex.deal.repository.CreditRepository;
import com.neoflex.deal.repository.StatementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @InjectMocks
    private DealService dealService;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private StatementRepository statementRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private CalculatorRequester calculatorRequester;
    @Mock
    private DealMapper dealMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLoanOfferDto_serialization_deserialization() throws Exception {
        LoanOfferDto dto = new LoanOfferDto();
        dto.setRate(BigDecimal.valueOf(8.5));
        dto.setRequestedAmount(BigDecimal.valueOf(100000));
        dto.setStatementId(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(dto);
        LoanOfferDto deserialized = objectMapper.readValue(json, LoanOfferDto.class);

        assertThat(deserialized.getRate()).isEqualTo(dto.getRate());
        assertThat(deserialized.getRequestedAmount()).isEqualTo(dto.getRequestedAmount());
        assertThat(deserialized.getStatementId()).isEqualTo(dto.getStatementId());
    }

    @Test
    void testCalculateLoanOffers_success() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        requestDto.setAmount(new BigDecimal("50000.00"));
        requestDto.setTerm(12);

        Client mappedClient = new Client();
        UUID clientId = UUID.randomUUID();
        mappedClient.setClientId(clientId);
        when(dealMapper.toClient(requestDto)).thenReturn(mappedClient);
        when(clientRepository.save(any(Client.class))).thenReturn(mappedClient);

        Statement savedStatement = new Statement();
        UUID statementId = UUID.randomUUID();
        savedStatement.setStatementId(statementId);
        savedStatement.setClient(mappedClient);
        when(statementRepository.save(any(Statement.class))).thenReturn(savedStatement);

        LoanOfferDto offer1 = new LoanOfferDto();
        offer1.setRate(BigDecimal.valueOf(10.5));
        LoanOfferDto offer2 = new LoanOfferDto();
        offer2.setRate(BigDecimal.valueOf(9.5));
        List<LoanOfferDto> offersList = Arrays.asList(offer1, offer2);
        when(calculatorRequester.getLoanOffers(requestDto)).thenReturn(offersList);

        // Вызов
        List<LoanOfferDto> result = dealService.calculateLoanOffers(requestDto);

        // Статус данных
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getStatementId()).isEqualTo(statementId);
        assertThat(result.get(0).getRate()).isGreaterThan(result.get(1).getRate());
        // Проверка, что сериализация с новым StatementId работает
        String json = assertDoesNotThrow(() -> objectMapper.writeValueAsString(result.get(0)));
        assertThat(json).contains(statementId.toString());

        verify(clientRepository).save(any(Client.class));
        verify(statementRepository, atLeastOnce()).save(any(Statement.class));
        verify(calculatorRequester).getLoanOffers(requestDto);
    }

    @Test
    void testCalculateLoanOffers_calculatorError() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        when(calculatorRequester.getLoanOffers(requestDto))
                .thenThrow(new RuntimeException("Ошибка при вызове микросервиса калькулятора"));

        assertThatThrownBy(() -> dealService.calculateLoanOffers(requestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ошибка при вызове микросервиса калькулятора");
    }

    @Test
    void testSelectLoanOffer_success() {
        UUID statementId = UUID.randomUUID();
        LoanOfferDto loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(statementId);

        Statement statement = new Statement();
        statement.setStatementId(statementId);
        statement.setStatus(ApplicationStatus.APPROVED);
        statement.setStatementStatusHistory(new ArrayList<>());

        LoanOffer mappedOffer = new LoanOffer();
        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));
        when(statementRepository.save(any(Statement.class))).thenAnswer(inv -> inv.getArgument(0));
        when(dealMapper.toLoanOffer(loanOfferDto)).thenReturn(mappedOffer);

        dealService.selectLoanOffer(loanOfferDto);

        verify(statementRepository).findById(statementId);
        verify(statementRepository).save(any(Statement.class));
        assertThat(statement.getStatus()).isEqualTo(ApplicationStatus.APPROVED);
        assertThat(statement.getAppliedOffer()).isNotNull();

        List<StatementStatusHistory> history = statement.getStatementStatusHistory();
        assertThat(history).isNotEmpty();
        StatementStatusHistory lastStatus = history.get(history.size() - 1);
        assertThat(lastStatus.getStatus()).isEqualTo(ApplicationStatus.APPROVED.name());
    }

    @Test
    void testSelectLoanOffer_statementNotFound() {
        UUID statementId = UUID.randomUUID();
        LoanOfferDto loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(statementId);

        when(statementRepository.findById(statementId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.selectLoanOffer(loanOfferDto))
                .isInstanceOf(StatementNotFoundException.class)
                .hasMessageContaining("Statement not found with id: " + statementId);

        verify(statementRepository).findById(statementId);
        verify(statementRepository, never()).save(any());
    }

    @Test
    void testFinishRegistration_success() {
        UUID statementId = UUID.randomUUID();

        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();
        finishDto.setGender(Gender.MALE);

        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        LoanOffer appliedOffer = new LoanOffer();
        Credit existingCredit = new Credit();
        existingCredit.setCreditId(UUID.randomUUID());
        Statement statement = new Statement();
        statement.setStatementId(statementId);
        statement.setClient(client);
        statement.setStatementStatusHistory(new ArrayList<>());
        statement.setAppliedOffer(appliedOffer);
        statement.setCredit(existingCredit);
        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));

        ScoringDataDto scoringData = new ScoringDataDto();
        when(dealMapper.toScoringDataDto(finishDto, client, appliedOffer)).thenReturn(scoringData);

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(BigDecimal.TEN);
        when(calculatorRequester.calculateCredit(scoringData)).thenReturn(creditDto);

        Credit credit = new Credit();
        when(dealMapper.toCredit(existingCredit, creditDto)).thenReturn(credit);
        when(creditRepository.save(any(Credit.class))).thenReturn(credit);
        when(statementRepository.save(any(Statement.class))).thenAnswer(inv -> inv.getArgument(0));
        when(clientRepository.findById(client.getClientId())).thenReturn(Optional.of(client));
        when(creditRepository.findById(existingCredit.getCreditId())).thenReturn(Optional.of(existingCredit));
        dealService.finishRegistration(statementId, finishDto);

        verify(statementRepository).findById(statementId);
        verify(calculatorRequester).calculateCredit(any(ScoringDataDto.class));
        verify(creditRepository).save(any(Credit.class));
        verify(statementRepository).save(any(Statement.class));
        assertThat(statement.getStatus()).isEqualTo(ApplicationStatus.CC_APPROVED);
        assertThat(statement.getCredit()).isNotNull();
        assertThat(statement.getStatementStatusHistory()).isNotEmpty();
        // Проверка сериализации Credit
        assertDoesNotThrow(() -> objectMapper.writeValueAsString(creditDto));
    }

    @Test
    void testFinishRegistration_statementNotFound() {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();
        when(statementRepository.findById(statementId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.finishRegistration(statementId, finishDto))
                .isInstanceOf(StatementNotFoundException.class)
                .hasMessageContaining("Statement not found with id: " + statementId);

        verify(statementRepository).findById(statementId);
        verify(calculatorRequester, never()).calculateCredit(any());
        verify(creditRepository, never()).save(any());
        verify(statementRepository, never()).save(any());
    }

    @Test
    void testFinishRegistration_calculatorError() {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();

        Client client = new Client();
        when(clientRepository.findById(client.getClientId())).thenReturn(Optional.of(client));
        LoanOffer appliedOffer = new LoanOffer();

        Statement statement = new Statement();
        statement.setStatementId(statementId);
        statement.setClient(client);
        statement.setAppliedOffer(appliedOffer);
        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));

        ScoringDataDto scoringData = new ScoringDataDto();
        when(dealMapper.toScoringDataDto(finishDto, client, appliedOffer)).thenReturn(scoringData);

        when(calculatorRequester.calculateCredit(scoringData))
                .thenThrow(new RuntimeException("Ошибка при вызове микросервиса калькулятора"));

        assertThatThrownBy(() -> dealService.finishRegistration(statementId, finishDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ошибка при вызове микросервиса калькулятора");

        verify(statementRepository).findById(statementId);
        verify(calculatorRequester).calculateCredit(any(ScoringDataDto.class));
        verify(creditRepository, never()).save(any());
        verify(statementRepository, never()).save(any());
    }

    /// /

    @Test
    void testFinishRegistration_clientNotFound() {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();

        Client client = new Client();
        client.setClientId(UUID.randomUUID());

        LoanOffer appliedOffer = new LoanOffer();
        Credit credit = new Credit();
        credit.setCreditId(UUID.randomUUID());

        Statement statement = new Statement();
        statement.setStatementId(statementId);
        statement.setClient(client);
        statement.setAppliedOffer(appliedOffer);
        statement.setCredit(credit);

        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));
        when(clientRepository.findById(client.getClientId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.finishRegistration(statementId, finishDto))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("Client not found with id");

        verify(statementRepository).findById(statementId);
        verify(clientRepository).findById(client.getClientId());
        verifyNoMoreInteractions(creditRepository, calculatorRequester, dealMapper);
    }

    @Test
    void testFinishRegistration_creditNotFound() {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();

        Client client = new Client();
        client.setClientId(UUID.randomUUID());

        LoanOffer appliedOffer = new LoanOffer();

        Credit credit = new Credit();
        credit.setCreditId(UUID.randomUUID());

        Statement statement = new Statement();
        statement.setStatementId(statementId);
        statement.setClient(client);
        statement.setAppliedOffer(appliedOffer);
        statement.setCredit(credit);

        ScoringDataDto scoringData = new ScoringDataDto();
        CreditDto creditDto = new CreditDto();

        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));
        when(clientRepository.findById(client.getClientId())).thenReturn(Optional.of(client));
        when(dealMapper.toScoringDataDto(finishDto, client, appliedOffer)).thenReturn(scoringData);
        when(calculatorRequester.calculateCredit(scoringData)).thenReturn(creditDto);
        when(creditRepository.findById(credit.getCreditId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.finishRegistration(statementId, finishDto))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("Credit not found");

        verify(statementRepository).findById(statementId);
        verify(clientRepository).findById(client.getClientId());
        verify(calculatorRequester).calculateCredit(scoringData);
        verify(creditRepository).findById(credit.getCreditId());
    }
}


