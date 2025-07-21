package com.neoflex.calculator.controller;

import com.neoflex.calculator.dto.CreditDto;
import com.neoflex.calculator.dto.LoanOfferDto;
import com.neoflex.calculator.dto.LoanStatementRequestDto;
import com.neoflex.calculator.dto.ScoringDataDto;
import com.neoflex.calculator.exception.*;
import com.neoflex.calculator.service.CalculatorService;
import com.neoflex.calculator.validation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Tag(name = "Calculator API", description = "Методы для расчёта кредитных предложений и параметров кредита")
public class CalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private final CalculatorService calculatorService;

    @PostMapping("/offers")
    @Operation(summary = "Рассчитать кредитные предложения",
            description = "Принимает данные заявки и возвращает список возможных кредитных предложений")
    public ResponseEntity<List<LoanOfferDto>> calculateOffers(@Valid @RequestBody LoanStatementRequestDto request) {
        logger.info("Endpoint /offers - calculateOffers: Входные данные для calculateOffers: {}", request);
        List<LoanOfferDto> offers = calculatorService.calculateOffers(request);
        logger.info("Endpoint /offers - calculateOffers: Результат calculateOffers: {}", offers);
        return ResponseEntity.ok(offers);
    }


    @PostMapping("/calc")
    @Operation(summary = "Рассчитать параметры кредита",
            description = "Выполняет скоринг и рассчитывает параметры кредита по данным клиента")
    public ResponseEntity<CreditDto> calculateCredit(@Valid @RequestBody ScoringDataDto scoringData) {
        logger.info("Endpoint /calc - calculateCredit:Входные данные для calculateCredit: {}", scoringData);
        CreditDto credit = calculatorService.calculateCredit(scoringData);
        logger.info("Endpoint /calc - calculateCredit:Результат calculateCredit: {}", credit);
        return ResponseEntity.ok(credit);
    }
}
