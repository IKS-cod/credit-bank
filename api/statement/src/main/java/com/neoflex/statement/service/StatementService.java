package com.neoflex.statement.service;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.integration.DealRequester;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {
    private static final Logger logger = LoggerFactory.getLogger(StatementService.class);

    private final DealRequester dealRequester;

    public List<LoanOfferDto> getOffers(@Valid LoanStatementRequestDto request) {

        logger.info("getOffers - Входные данные: {}", request);
        List<LoanOfferDto> offers = dealRequester.getLoanOffers(request);
        logger.debug("getOffers - Получено предложений: {}", offers != null ? offers.size() : 0);
        logger.info("getOffers - Полученные предложения: {}", offers);
        return offers;


    }

    public void selectOffer(LoanOfferDto offerDto) {
        logger.info("selectOffer - Входные данные: {}", offerDto);
        logger.debug("selectOffer - Отправка выбранного предложения в микросервис deal");
        dealRequester.selectLoanOffer(offerDto);
        logger.info("selectOffer - Выбранное предложение успешно отправлено");
    }
}
