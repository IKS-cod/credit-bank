package com.neoflex.deal.integration;

import com.neoflex.deal.dto.CreditDto;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.dto.ScoringDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalculatorRequester {
    private final RestTemplate restTemplate;

    @Value("${calculator.offers.url}")
    private String calculatorOffersUrl;

    @Value("${calculator.calc.url}")
    private String calculatorCalcUrl;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto requestDto) {
        ResponseEntity<LoanOfferDto[]> response = restTemplate.postForEntity(
                calculatorOffersUrl, requestDto, LoanOfferDto[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        }
        throw new RuntimeException("Ошибка при вызове микросервиса калькулятора");
    }

    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        ResponseEntity<CreditDto> response = restTemplate.postForEntity(
                calculatorCalcUrl, scoringData, CreditDto.class
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        throw new RuntimeException("Ошибка при вызове микросервиса калькулятора");
    }
}

