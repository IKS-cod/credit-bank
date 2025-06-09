package com.neoflex.calculator.controller;

import com.neoflex.calculator.dto.CreditDto;
import com.neoflex.calculator.dto.LoanOfferDto;
import com.neoflex.calculator.dto.LoanStatementRequestDto;
import com.neoflex.calculator.dto.ScoringDataDto;
import com.neoflex.calculator.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    @Test
    void calculateOffers_ShouldReturnLoanOffers() {
        // Arrange
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        List<LoanOfferDto> expectedOffers = List.of(new LoanOfferDto());
        when(calculatorService.calculateOffers(request)).thenReturn(expectedOffers);

        // Act
        ResponseEntity<List<LoanOfferDto>> response = calculatorController.calculateOffers(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedOffers, response.getBody());
        verify(calculatorService, times(1)).calculateOffers(request);
    }

    @Test
    void calculateCredit_ShouldReturnCreditParameters() {
        // Arrange
        ScoringDataDto scoringData = new ScoringDataDto();
        CreditDto expectedCredit = new CreditDto();
        when(calculatorService.calculateCredit(scoringData)).thenReturn(expectedCredit);

        // Act
        ResponseEntity<CreditDto> response = calculatorController.calculateCredit(scoringData);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedCredit, response.getBody());
        verify(calculatorService, times(1)).calculateCredit(scoringData);
    }
}
