package com.neoflex.deal.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neoflex.deal.dto.EmploymentDto;
import com.neoflex.deal.dto.FinishRegistrationRequestDto;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.enums.EmploymentPosition;
import com.neoflex.deal.enums.EmploymentStatus;
import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.enums.MaritalStatus;
import com.neoflex.deal.exception.GlobalExceptionHandler;
import com.neoflex.deal.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DealControllerTest {

    @InjectMocks
    private DealController dealController;

    @Mock
    private DealService dealService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders
                .standaloneSetup(dealController)
                .setControllerAdvice(new GlobalExceptionHandler(objectMapper))
                .build();
    }

    @Test
    public void testCalculateLoanOffers_success() throws Exception {
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

        // Мокаем результат
        LoanOfferDto offer1 = new LoanOfferDto();
        offer1.setRate(BigDecimal.valueOf(10.5));
        offer1.setRequestedAmount(new BigDecimal("50000.00"));
        offer1.setTerm(12);

        LoanOfferDto offer2 = new LoanOfferDto();
        offer2.setRate(BigDecimal.valueOf(9.5));
        offer2.setRequestedAmount(new BigDecimal("50000.00"));
        offer2.setTerm(12);

        List<LoanOfferDto> mockOffers = List.of(offer1, offer2);
        when(dealService.calculateLoanOffers(any(LoanStatementRequestDto.class))).thenReturn(mockOffers);

        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Сериализация — проверим поля первого объекта json-ответа
                .andExpect(jsonPath("$[0].rate").value(10.5))
                .andExpect(jsonPath("$[1].rate").value(9.5))
                .andExpect(jsonPath("$[0].requestedAmount").value(50000.00))
                .andExpect(jsonPath("$.length()").value(2));

        verify(dealService, times(1)).calculateLoanOffers(any(LoanStatementRequestDto.class));
    }

    @Test
    public void testCalculateLoanOffers_internalServerError() throws Exception {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        requestDto.setAmount(new BigDecimal("1000.00"));
        when(dealService.calculateLoanOffers(any(LoanStatementRequestDto.class)))
                .thenThrow(new RuntimeException("unexpected error"));

        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("unexpected error")));

        verify(dealService, times(1)).calculateLoanOffers(any());
    }

    @Test
    public void testSelectLoanOffer_success() throws Exception {
        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setRequestedAmount(new BigDecimal("50000.00"));
        offerDto.setTotalAmount(new BigDecimal("55000.00"));
        offerDto.setStatementId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        offerDto.setTerm(12);
        offerDto.setMonthlyPayment(new BigDecimal("4583.33"));
        offerDto.setRate(new BigDecimal("12.5"));
        offerDto.setIsInsuranceEnabled(true);
        offerDto.setIsSalaryClient(false);

        doNothing().when(dealService).selectLoanOffer(any(LoanOfferDto.class));

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(dealService, times(1)).selectLoanOffer(any(LoanOfferDto.class));
    }

    @Test
    public void testSelectLoanOffer_error() throws Exception {
        LoanOfferDto offerDto = new LoanOfferDto();
        doThrow(new RuntimeException("select error"))
                .when(dealService).selectLoanOffer(any(LoanOfferDto.class));

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("select error")));
    }


    @Test
    public void testFinishRegistration_success() throws Exception {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();
        finishDto.setGender(Gender.MALE);
        finishDto.setMaritalStatus(MaritalStatus.SINGLE);
        finishDto.setDependentAmount(2);
        finishDto.setPassportIssueDate(LocalDate.of(2010, 5, 20));
        finishDto.setPassportIssueBranch("Moscow Branch");

        EmploymentDto employmentDto = new EmploymentDto();
        employmentDto.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employmentDto.setEmployerINN("7707083893");
        employmentDto.setSalary(new BigDecimal("75000.00"));
        employmentDto.setPosition(EmploymentPosition.MIDDLE_MANAGER);
        employmentDto.setWorkExperienceTotal(120);
        employmentDto.setWorkExperienceCurrent(24);

        finishDto.setEmployment(employmentDto);
        finishDto.setAccountNumber("40817810099910004312");

        doNothing().when(dealService).finishRegistration(eq(statementId), any(FinishRegistrationRequestDto.class));

        mockMvc.perform(post("/deal/calculate/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(dealService, times(1)).finishRegistration(eq(statementId), any(FinishRegistrationRequestDto.class));
    }

    @Test
    public void testFinishRegistration_error() throws Exception {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto finishDto = new FinishRegistrationRequestDto();
        doThrow(new RuntimeException("finish error"))
                .when(dealService).finishRegistration(eq(statementId), any(FinishRegistrationRequestDto.class));

        mockMvc.perform(post("/deal/calculate/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("finish error")));
    }

}




