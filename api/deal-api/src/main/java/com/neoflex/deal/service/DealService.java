package com.neoflex.deal.service;

import com.neoflex.deal.dto.*;
import com.neoflex.deal.enums.ApplicationStatus;
import com.neoflex.deal.enums.ChangeType;
import com.neoflex.deal.exception.ClientNotFoundException;
import com.neoflex.deal.exception.StatementNotFoundException;
import com.neoflex.deal.integration.CalculatorRequester;
import com.neoflex.deal.mapper.DealMapper;
import com.neoflex.deal.model.*;
import com.neoflex.deal.repository.ClientRepository;
import com.neoflex.deal.repository.CreditRepository;
import com.neoflex.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DealService {
    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final CalculatorRequester calculatorRequester;
    private final DealMapper dealMapper;

    @Transactional
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto requestDto) {
        logger.info("calculateLoanOffers - Входные данные: {}", requestDto);

        Client client = saveClient(requestDto);
        Statement statement = createAndSaveStatement(client, requestDto);

        List<LoanOfferDto> offers = calculatorRequester.getLoanOffers(requestDto);
        logger.debug("Получены предложения от калькулятора: {}", offers);

        addStatementIdToOffers(statement.getStatementId(), offers);
        sortOffersByRateDesc(offers);
        return offers;
    }

    @Transactional
    public void selectLoanOffer(LoanOfferDto loanOfferDto) {
        logger.info("selectLoanOffer - Входные данные: {}", loanOfferDto);

        Statement statement = findStatement(loanOfferDto.getStatementId());
        statement.setStatus(ApplicationStatus.APPROVED);

        addStatusHistory(statement, ApplicationStatus.APPROVED, ChangeType.AUTOMATIC);

        statement.setAppliedOffer(dealMapper.toLoanOffer(loanOfferDto));
        // Задержка удерживает блокировку и транзакцию
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        statementRepository.save(statement);
        logger.info("Заявка обновлена и сохранена: {}", statement);
    }

    @Transactional
    public void finishRegistration(UUID statementIdStr, FinishRegistrationRequestDto finishDto) {
        logger.info("statementIdStr: {}", statementIdStr);
        logger.info("finishDto: {}", finishDto);
        logger.info("finishRegistration - Входные данные: statementId={}, finishDto={}", statementIdStr, finishDto);
        Statement statement = findStatement(statementIdStr);

        Client clientFromBD = findClient(statement.getClient().getClientId());
        logger.info("Получен Client: {}", clientFromBD);
        Client clientForUpdate = updateClient(clientFromBD, finishDto);
        clientRepository.save(clientForUpdate);
        logger.info("Client сохранён: {}", clientForUpdate);

        ScoringDataDto scoringData = dealMapper.toScoringDataDto(
                finishDto, statement.getClient(), statement.getAppliedOffer());
        logger.debug("Создан ScoringDataDto: {}", scoringData);

        CreditDto creditDto = calculatorRequester.calculateCredit(scoringData);
        logger.debug("Получен CreditDto: {}", creditDto);

        Credit creditFromBD = findCredit(statement.getCredit().getCreditId());
        logger.info("Получен Credit: {}", creditFromBD);

        Credit credit = dealMapper.toCredit(creditFromBD, creditDto);
        creditRepository.save(credit);
        logger.info("Credit сохранён: {}", credit);

        statement.setCredit(credit);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        addStatusHistory(statement, ApplicationStatus.CC_APPROVED, ChangeType.AUTOMATIC);

        statementRepository.save(statement);
        logger.info("finishRegistration - Заявка обновлена и сохранена: {}", statement);
    }

    private Credit findCredit(UUID creditId) {
        return creditRepository.findById(creditId)
                .orElseThrow(() -> {
                    logger.debug("Credit не найден с id: {}", creditId);
                    return new ClientNotFoundException("Credit not found with id: " + creditId);
                });
    }

    private Client findClient(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.debug("Client не найден с id: {}", clientId);
                    return new ClientNotFoundException("Client not found with id: " + clientId);
                });
    }

    private Client updateClient(Client client, FinishRegistrationRequestDto finishDto) {
        client.setDependentAmount(finishDto.getDependentAmount());
        client.setAccountNumber(finishDto.getAccountNumber());
        client.setGender(finishDto.getGender());
        client.setMaritalStatus(finishDto.getMaritalStatus());
        Passport passport = client.getPassport();
        passport.setIssueDate(finishDto.getPassportIssueDate());
        passport.setIssueBranch(finishDto.getPassportIssueBranch());
        client.setPassport(passport);
        Employment employment = dealMapper.toEmployment(finishDto);
        logger.debug("Создан Employment: {}", employment);
        client.setEmployment(employment);
        return client;
    }

    private Client saveClient(LoanStatementRequestDto requestDto) {
        Client client = dealMapper.toClient(requestDto);
        return clientRepository.save(client);
    }

    private Statement createAndSaveStatement(Client client, LoanStatementRequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now();

        Statement statement = new Statement();
        statement.setStatementId(null);
        statement.setClient(client);
        statement.setCreationDate(now);
        statement.setStatus(ApplicationStatus.PREAPPROVAL);

        Credit credit = new Credit();
        credit.setAmount(requestDto.getAmount());
        credit.setTerm(requestDto.getTerm());
        credit = creditRepository.save(credit);
        statement.setCredit(credit);

        statement.setStatementStatusHistory(
                new ArrayList<>(List.of(createStatusHistory(ApplicationStatus.PREAPPROVAL, now, ChangeType.AUTOMATIC)))
        );

        return statementRepository.save(statement);
    }

    private void addStatementIdToOffers(UUID statementId, List<LoanOfferDto> offers) {
        offers.forEach(offer -> offer.setStatementId(statementId));
        logger.debug("StatementId присвоен каждому предложению");
    }

    private void sortOffersByRateDesc(List<LoanOfferDto> offers) {
        offers.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());
        logger.info("Отсортированные предложения: {}", offers);
    }

    private Statement findStatement(UUID statementId) {
        return statementRepository.findById(statementId)
                .orElseThrow(() -> {
                    logger.debug("Statement не найден с id: {}", statementId);
                    return new StatementNotFoundException("Statement not found with id: " + statementId);
                });
    }

    private void addStatusHistory(Statement statement, ApplicationStatus status, ChangeType changeType) {
        List<StatementStatusHistory> history = Optional.ofNullable(statement.getStatementStatusHistory())
                .map(ArrayList::new).orElseGet(ArrayList::new);

        history.add(createStatusHistory(status, LocalDateTime.now(), changeType));
        statement.setStatementStatusHistory(history);
        logger.debug("Добавлена запись в историю статусов: {}", status);
    }

    private StatementStatusHistory createStatusHistory(ApplicationStatus status, LocalDateTime time, ChangeType changeType) {
        StatementStatusHistory h = new StatementStatusHistory();
        h.setStatus(status.name());
        h.setTime(time);
        h.setChangeType(changeType);
        return h;
    }
}


