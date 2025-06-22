package com.neoflex.deal.controller;

import com.neoflex.deal.dto.FinishRegistrationRequestDto;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deal")
@Slf4j
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    /**
     * Расчёт возможных условий кредита по заявке
     */
    @Operation(
            summary = "Рассчитать кредитные предложения",
            description = "Принимает данные заявки и возвращает список возможных кредитных предложений")
    @PostMapping("/statement")
    public List<LoanOfferDto> calculateLoanOffers(@RequestBody LoanStatementRequestDto request) {
        log.info("POST /deal/statement - Входные данные: {}", request);
        List<LoanOfferDto> offers = dealService.calculateLoanOffers(request);
        log.info("POST /deal/statement - Результат: {} предложений", offers.size());
        return offers;
    }

    /**
     * Выбор одного из предложений
     */
    @Operation(
            summary = "Выбрать кредитное предложение",
            description = "Принимает выбранное кредитное предложение")
    @PostMapping("/offer/select")
    public void selectLoanOffer(@RequestBody LoanOfferDto offer) {
        log.info("POST /deal/offer/select - Входные данные: {}", offer);
        dealService.selectLoanOffer(offer);
        log.info("POST /deal/offer/select - Выбор предложения завершён");
    }

    /**
     * Завершение регистрации и полный подсчёт кредита
     */
    @Operation(
            summary = "Завершить регистрацию и рассчитать кредит",
            description = "Принимает ID заявки и данные для завершения регистрации, возвращает void")
    @PostMapping("/calculate/{statementId}")
    public void finishRegistration(
            @Parameter(description = "ID заявки", required = true)
            @PathVariable String statementId,
            @RequestBody FinishRegistrationRequestDto request) {
        log.info("POST /deal/calculate/{} - Входные данные: {}", statementId, request);
        dealService.finishRegistration(statementId, request);
        log.info("POST /deal/calculate/{} - Завершение регистрации выполнено", statementId);
    }
}


