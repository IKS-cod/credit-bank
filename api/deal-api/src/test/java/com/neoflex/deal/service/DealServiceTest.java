package com.neoflex.deal.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import com.neoflex.deal.dto.*;
import com.neoflex.deal.enums.*;
import com.neoflex.deal.exception.StatementNotFoundException;
import com.neoflex.deal.model.*;
import com.neoflex.deal.repository.ClientRepository;
import com.neoflex.deal.repository.CreditRepository;
import com.neoflex.deal.repository.StatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DealServiceTest {

    @InjectMocks
    private DealService dealService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateLoanOffers_success() {
        // Подготовка входных данных
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

        // Мокируем сохранение клиента
        Client savedClient = new Client();
        savedClient.setClientId(UUID.randomUUID());
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // Мокируем сохранение заявки
        Statement savedStatement = new Statement();
        UUID statementId = UUID.randomUUID();
        savedStatement.setStatementId(statementId);
        savedStatement.setClient(savedClient);
        when(statementRepository.save(any(Statement.class))).thenReturn(savedStatement);

        // Мокируем ответ от RestTemplate
        LoanOfferDto offer1 = new LoanOfferDto();
        offer1.setRate(BigDecimal.valueOf(10.5));
        LoanOfferDto offer2 = new LoanOfferDto();
        offer2.setRate(BigDecimal.valueOf(9.5));
        LoanOfferDto[] offersArray = new LoanOfferDto[]{offer1, offer2};

        ResponseEntity<LoanOfferDto[]> responseEntity = new ResponseEntity<>(offersArray, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(LoanOfferDto[].class))).thenReturn(responseEntity);

        // Вызов тестируемого метода
        List<LoanOfferDto> result = dealService.calculateLoanOffers(requestDto);

        // Проверки
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        // Проверяем, что statementId присвоен каждому предложению
        assertThat(result.get(0).getStatementId()).isEqualTo(statementId);
        assertThat(result.get(1).getStatementId()).isEqualTo(statementId);
        // Проверяем сортировку по убыванию ставки
        assertThat(result.get(0).getRate()).isGreaterThan(result.get(1).getRate());

        // Проверяем вызовы моков
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(statementRepository, times(1)).save(any(Statement.class));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(LoanOfferDto[].class));
    }

    @Test
    public void testCalculateLoanOffers_calculatorError() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        ResponseEntity<LoanOfferDto[]> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.postForEntity(anyString(), any(), eq(LoanOfferDto[].class))).thenReturn(responseEntity);

        assertThatThrownBy(() -> dealService.calculateLoanOffers(requestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ошибка при вызове микросервиса калькулятора");
    }

    @Test
    public void testSelectLoanOffer_success() {
        UUID statementId = UUID.randomUUID();

        // Подготовка входных данных
        LoanOfferDto loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(statementId);
        loanOfferDto.setRequestedAmount(new BigDecimal("50000"));
        loanOfferDto.setTotalAmount(new BigDecimal("55000"));
        loanOfferDto.setTerm(12);
        loanOfferDto.setMonthlyPayment(new BigDecimal("4583.33"));
        loanOfferDto.setRate(new BigDecimal("12.5"));
        loanOfferDto.setIsInsuranceEnabled(true);
        loanOfferDto.setIsSalaryClient(false);

        // Подготовка существующей заявки
        Statement statement = new Statement();
        statement.setStatementId(statementId);
        statement.setStatus(ApplicationStatus.APPROVED);
        statement.setStatementStatusHistory(new ArrayList<>());

        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));
        when(statementRepository.save(any(Statement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Вызов тестируемого метода
        dealService.selectLoanOffer(loanOfferDto);

        // Проверки
        verify(statementRepository, times(1)).findById(statementId);
        verify(statementRepository, times(1)).save(any(Statement.class));

        assertThat(statement.getStatus()).isEqualTo(ApplicationStatus.PREAPPROVAL);

        List<StatementStatusHistory> history = statement.getStatementStatusHistory();
        assertThat(history).isNotEmpty();
        StatementStatusHistory lastStatus = history.get(history.size() - 1);
        assertThat(lastStatus.getStatus()).isEqualTo(ApplicationStatus.APPROVED.name());
        assertThat(lastStatus.getChangeType()).isEqualTo(ChangeType.MANUAL);
        assertThat(lastStatus.getTime()).isNotNull();

        LoanOffer appliedOffer = statement.getAppliedOffer();
        assertThat(appliedOffer).isNotNull();
        assertThat(appliedOffer.getStatementId()).isEqualTo(statementId);
        assertThat(appliedOffer.getRequestedAmount()).isEqualByComparingTo("50000");
    }

    @Test
    public void testSelectLoanOffer_statementNotFound() {
        UUID statementId = UUID.randomUUID();
        LoanOfferDto loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(statementId);

        when(statementRepository.findById(statementId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.selectLoanOffer(loanOfferDto))
                .isInstanceOf(StatementNotFoundException.class)
                .hasMessageContaining("Statement not found with id: " + statementId);

        verify(statementRepository, times(1)).findById(statementId);
        verify(statementRepository, never()).save(any());
    }

//-----------------------------
@Test
public void testFinishRegistration_success() {
    UUID statementId = UUID.randomUUID();
    String statementIdStr = statementId.toString();

    // Подготовка входных данных
    FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();
    finishDto.setGender(Gender.MALE);
    finishDto.setMaritalStatus(MaritalStatus.SINGLE);
    finishDto.setDependentAmount(1);
    finishDto.setPassportIssueDate(LocalDate.of(2010, 1, 1));
    finishDto.setPassportIssueBranch("Branch");
    finishDto.setAccountNumber("40817810099910004312");
    EmploymentDto employmentDto = new EmploymentDto();
    employmentDto.setEmploymentStatus(EmploymentStatus.EMPLOYED);
    employmentDto.setEmployerINN("1234567890");
    employmentDto.setSalary(new BigDecimal("75000"));
    employmentDto.setPosition(EmploymentPosition.MID_MANAGER);
    employmentDto.setWorkExperienceTotal(60);
    employmentDto.setWorkExperienceCurrent(24);
    finishDto.setEmployment(employmentDto);

    // Подготовка клиента и заявки
    Client client = new Client();
    client.setFirstName("John");
    client.setLastName("Doe");
    client.setMiddleName("M");
    client.setBirthDate(LocalDate.of(1990, 5, 15));
    Passport passport = new Passport();
    passport.setSeries("1234");
    passport.setNumber("567890");
    client.setPassport(passport);

    Statement statement = new Statement();
    statement.setStatementId(statementId);
    statement.setClient(client);
    statement.setStatementStatusHistory(new ArrayList<>());

    when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));

    // Подготовка ответа микросервиса калькулятора
    CreditDto creditDto = new CreditDto();
    creditDto.setAmount(new BigDecimal("50000"));
    creditDto.setTerm(12);
    creditDto.setMonthlyPayment(new BigDecimal("4500"));
    creditDto.setRate(new BigDecimal("12.5"));
    creditDto.setPsk(new BigDecimal("15.0"));
    creditDto.setIsInsuranceEnabled(true);
    creditDto.setIsSalaryClient(false);

    PaymentScheduleElementDto payment1 = new PaymentScheduleElementDto();
    payment1.setNumber(1);
    payment1.setDate(LocalDate.of(2025, 7, 1));
    payment1.setTotalPayment(new BigDecimal("4500"));
    payment1.setInterestPayment(new BigDecimal("500"));
    payment1.setDebtPayment(new BigDecimal("4000"));
    payment1.setRemainingDebt(new BigDecimal("46000"));

    creditDto.setPaymentSchedule(List.of(payment1));

    ResponseEntity<CreditDto> responseEntity = new ResponseEntity<>(creditDto, HttpStatus.OK);
    when(restTemplate.postForEntity(anyString(), any(), eq(CreditDto.class))).thenReturn(responseEntity);

    when(creditRepository.save(any(Credit.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(statementRepository.save(any(Statement.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Вызов тестируемого метода
    dealService.finishRegistration(statementIdStr, finishDto);

    // Проверки
    verify(statementRepository, times(1)).findById(statementId);
    verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(CreditDto.class));
    verify(creditRepository, times(1)).save(any(Credit.class));
    verify(statementRepository, times(1)).save(any(Statement.class));

    assertThat(statement.getStatus()).isEqualTo(ApplicationStatus.PREPARE_DOCUMENTS);
    assertThat(statement.getCredit()).isNotNull();
    assertThat(statement.getStatementStatusHistory()).isNotEmpty();

    StatementStatusHistory lastStatus = statement.getStatementStatusHistory().get(statement.getStatementStatusHistory().size() - 1);
    assertThat(lastStatus.getStatus()).isEqualTo(ApplicationStatus.PREPARE_DOCUMENTS.name());
    assertThat(lastStatus.getChangeType()).isEqualTo(ChangeType.MANUAL);
    assertThat(lastStatus.getTime()).isNotNull();
}

    @Test
    public void testFinishRegistration_statementNotFound() {
        UUID statementId = UUID.randomUUID();
        String statementIdStr = statementId.toString();
        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();

        when(statementRepository.findById(statementId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealService.finishRegistration(statementIdStr, finishDto))
                .isInstanceOf(StatementNotFoundException.class)
                .hasMessageContaining("Statement not found with id: " + statementId);

        verify(statementRepository, times(1)).findById(statementId);
        verify(restTemplate, never()).postForEntity(anyString(), any(), eq(CreditDto.class));
        verify(creditRepository, never()).save(any());
        verify(statementRepository, never()).save(any());
    }

    @Test
    public void testFinishRegistration_calculatorError() {
        UUID statementId = UUID.randomUUID();
        String statementIdStr = statementId.toString();

        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();

        Client client = new Client();
        Passport passport = new Passport();
        passport.setSeries("1234");
        passport.setNumber("567890");
        client.setPassport(passport);

        Statement statement = new Statement();
        statement.setStatementId(statementId);
        statement.setClient(client);

        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));

        ResponseEntity<CreditDto> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.postForEntity(anyString(), any(), eq(CreditDto.class))).thenReturn(responseEntity);

        assertThatThrownBy(() -> dealService.finishRegistration(statementIdStr, finishDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ошибка при вызове микросервиса калькулятора");

        verify(statementRepository, times(1)).findById(statementId);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(CreditDto.class));
        verify(creditRepository, never()).save(any());
        verify(statementRepository, never()).save(any());
    }



}
