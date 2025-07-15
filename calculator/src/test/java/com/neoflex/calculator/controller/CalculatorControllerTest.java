package com.neoflex.calculator.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

import com.neoflex.calculator.dto.CreditDto;
import com.neoflex.calculator.dto.LoanOfferDto;
import com.neoflex.calculator.dto.LoanStatementRequestDto;
import com.neoflex.calculator.dto.ScoringDataDto;
import com.neoflex.calculator.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateOffers_ShouldReturnListOfLoanOfferDto() {
        // Arrange
        LoanStatementRequestDto request = new LoanStatementRequestDto();

        LoanOfferDto offer1 = new LoanOfferDto();
        LoanOfferDto offer2 = new LoanOfferDto();
        List<LoanOfferDto> offers = Arrays.asList(offer1, offer2);

        when(calculatorService.calculateOffers(request)).thenReturn(offers);

        // Act
        ResponseEntity<List<LoanOfferDto>> response = calculatorController.calculateOffers(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(offers);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(calculatorService, times(1)).calculateOffers(request);
    }

    @Test
    void calculateCredit_ShouldReturnCreditDto() {
        // Arrange
        ScoringDataDto scoringData = new ScoringDataDto();

        CreditDto creditDto = new CreditDto();
        when(calculatorService.calculateCredit(scoringData)).thenReturn(creditDto);

        // Act
        ResponseEntity<CreditDto> response = calculatorController.calculateCredit(scoringData);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(creditDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(calculatorService, times(1)).calculateCredit(scoringData);
    }
}


