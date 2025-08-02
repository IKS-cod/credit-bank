package com.neoflex.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.service.StatementService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StatementController.class)
class StatementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatementService statementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getOffers_ShouldReturnListOfLoanOfferDto() throws Exception {
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

        when(statementService.getOffers(any(LoanStatementRequestDto.class))).thenReturn(mockOffers);

        // when / then
        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(mockOffers.size()))
                // Проверка полей первого объекта
                .andExpect(jsonPath("$[0].statementId").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$[0].requestedAmount").value(50000.00))
                .andExpect(jsonPath("$[0].totalAmount").value(55000.00))
                .andExpect(jsonPath("$[0].term").value(12))
                .andExpect(jsonPath("$[0].monthlyPayment").value(4583.33))
                .andExpect(jsonPath("$[0].rate").value(12.5))
                .andExpect(jsonPath("$[0].isInsuranceEnabled").value(true))
                .andExpect(jsonPath("$[0].isSalaryClient").value(false))
                // Проверка полей второго объекта
                .andExpect(jsonPath("$[1].statementId").value("321e6547-e89b-12d3-a456-426614174999"))
                .andExpect(jsonPath("$[1].requestedAmount").value(75000.00))
                .andExpect(jsonPath("$[1].totalAmount").value(84000.00))
                .andExpect(jsonPath("$[1].term").value(24))
                .andExpect(jsonPath("$[1].monthlyPayment").value(3500.00))
                .andExpect(jsonPath("$[1].rate").value(11.0))
                .andExpect(jsonPath("$[1].isInsuranceEnabled").value(false))
                .andExpect(jsonPath("$[1].isSalaryClient").value(true));

        // verify that service was called with correct argument
        ArgumentCaptor<LoanStatementRequestDto> captor = ArgumentCaptor.forClass(LoanStatementRequestDto.class);
        verify(statementService).getOffers(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(requestDto);
    }

    @Test
    void selectOffer_ShouldReturnNoContent() throws Exception {
        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(new BigDecimal("50000.00"));
        offerDto.setTerm(12);

        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andExpect(status().isNoContent());

        ArgumentCaptor<LoanOfferDto> captor = ArgumentCaptor.forClass(LoanOfferDto.class);
        verify(statementService).selectOffer(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(offerDto);
    }

}
