package com.neoflex.statement.integration;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DealRequester {
    private final RestTemplate restTemplate;

    @Value("${deal.offers.url}")
    private String dealOffersUrl;

    @Value("${deal.selectLoanOffer.url}")
    private String dealSelectLoanOfferUrl;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto requestDto) {
        ResponseEntity<LoanOfferDto[]> response = restTemplate.postForEntity(
                dealOffersUrl, requestDto, LoanOfferDto[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        }
        throw new RuntimeException("Ошибка при вызове микросервиса калькулятора");
    }

    public void selectLoanOffer(LoanOfferDto selectedOffer) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                dealSelectLoanOfferUrl, selectedOffer, Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Ошибка при отправке выбранного предложения в микросервис deal");
        }
    }
}
