package com.neoflex.statement.controller;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.service.StatementService;
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
@RequestMapping("/statement")
@RequiredArgsConstructor
@Tag(name = "Statement API", description = "Управление заявками")
public class StatementController {

    private static final Logger logger = LoggerFactory.getLogger(StatementController.class);
    private final StatementService statementService;

    @PostMapping()
    @Operation(summary = "Получить кредитные предложения",
            description = "Принимает данные заявки и возвращает список возможных кредитных предложений")
    public ResponseEntity<List<LoanOfferDto>> getOffers(@Valid @RequestBody LoanStatementRequestDto request) {
        logger.info("Endpoint /statement - getOffers: Входные данные: {}", request);
        List<LoanOfferDto> offers = statementService.getOffers(request);
        logger.info("Endpoint /statement - getOffers: Результат: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer")
    @Operation(summary = "Выбор кредитного предложения")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto offerDto) {
        logger.info("Endpoint /statement/offer - selectOffer: Входные данные: {}", offerDto);
        statementService.selectOffer(offerDto);
        logger.info("Endpoint /statement/offer - selectOffer: Завершено успешно");
        return ResponseEntity.noContent().build();
    }
}

