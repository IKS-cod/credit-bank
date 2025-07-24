package com.neoflex.deal.service;

import com.neoflex.deal.dto.*;
import com.neoflex.deal.enums.ApplicationStatus;
import com.neoflex.deal.enums.ChangeType;
import com.neoflex.deal.enums.CreditStatus;
import com.neoflex.deal.exception.StatementNotFoundException;
import com.neoflex.deal.integration.CalculatorRequester;
import com.neoflex.deal.mapper.DealMapper;
import com.neoflex.deal.model.Client;
import com.neoflex.deal.model.Credit;
import com.neoflex.deal.model.Statement;
import com.neoflex.deal.model.StatementStatusHistory;
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

//@Service
//@RequiredArgsConstructor
//public class DealService {
//    private static final Logger logger = LoggerFactory.getLogger(DealService.class);
//
//    private final ClientRepository clientRepository;
//    private final StatementRepository statementRepository;
//    private final RestTemplate restTemplate;
//    private final CreditRepository creditRepository;
//
//    private static final String CALCULATOR_URL = "http://localhost:8081/calculator/offers";
//    private static final String CALCULATOR_CALC_URL = "http://localhost:8081/calculator/calc";
//
//    @Transactional
//    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto requestDto) {
//        logger.info("calculateLoanOffers - Входные данные: {}", requestDto);
//
//        // Создаём и сохраняем Client
//        Client client = mapToClient(requestDto);
//        client = clientRepository.save(client);
//        logger.debug("Client сохранён: {}", client);
//
//        // Создаём и сохраняем Statement
//        Statement statement = new Statement();
//        statement.setStatementId(null);
//        statement.setClient(client);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        statement.setCreationDate(localDateTime);
//        ApplicationStatus applicationStatus = ApplicationStatus.DOCUMENT_CREATED;
//        statement.setStatus(applicationStatus);
//
//        Credit credit = new Credit();
//        credit.setAmount(requestDto.getAmount());
//        credit.setTerm(requestDto.getTerm());
//        credit.setCreditStatus(CreditStatus.CALCULATED);
//        credit = creditRepository.save(credit);
//        statement.setCredit(credit);
//        StatementStatusHistory statementStatusHistory = new StatementStatusHistory();
//        statementStatusHistory.setTime(localDateTime);
//        statementStatusHistory.setStatus(applicationStatus.toString());
//        statementStatusHistory.setChangeType(ChangeType.AUTOMATIC);
//        List<StatementStatusHistory> statementStatusHistoryList = new ArrayList<>();
//        statementStatusHistoryList.add(statementStatusHistory);
//        statement.setStatementStatusHistory(statementStatusHistoryList);
//
//        statement = statementRepository.save(statement);
//        logger.debug("Statement сохранён: {}", statement);
//
//        // Запрос к микросервису калькулятора
//        List<LoanOfferDto> offers = fetchLoanOffersFromCalculator(requestDto);
//        logger.debug("Получены предложения от калькулятора: {}", offers);
//
//        // Присваиваем statementId каждому предложению
//        Statement finalStatement = statement;
//        offers.forEach(offer -> offer.setStatementId(finalStatement.getStatementId()));
//        logger.debug("StatementId присвоен каждому предложению");
//
//        // Сортируем от "худшего" к "лучшему"
//        offers.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());
//        logger.info("Отсортированные предложения: {}", offers);
//
//        return offers;
//    }
//
//    private Client mapToClient(LoanStatementRequestDto dto) {
//        Client client = new Client();
//        client.setClientId(null);
//        client.setFirstName(dto.getFirstName());
//        client.setLastName(dto.getLastName());
//        client.setMiddleName(dto.getMiddleName());
//        client.setEmail(dto.getEmail());
//        client.setBirthDate(dto.getBirthdate());
//
//        Passport passport = new Passport();
//        passport.setNumber(dto.getPassportNumber());
//        passport.setSeries(dto.getPassportSeries());
//        client.setPassport(passport);
//
//        logger.debug("Преобразован DTO в Client: {}", client);
//        return client;
//    }
//
//    private List<LoanOfferDto> fetchLoanOffersFromCalculator(LoanStatementRequestDto requestDto) {
//        logger.info("Отправка запроса в микросервис калькулятора с данными: {}", requestDto);
//
//        ResponseEntity<LoanOfferDto[]> response = restTemplate.postForEntity(
//                CALCULATOR_URL,
//                requestDto,
//                LoanOfferDto[].class
//        );
//
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            List<LoanOfferDto> offers = Arrays.asList(response.getBody());
//            logger.info("Получен успешный ответ от микросервиса калькулятора: {} предложений", offers.size());
//            return offers;
//        } else {
//            logger.error("Ошибка при вызове микросервиса калькулятора, статус: {}", response.getStatusCode());
//            throw new RuntimeException("Ошибка при вызове микросервиса калькулятора");
//        }
//    }
//
//
//    @Transactional
//    public void selectLoanOffer(LoanOfferDto loanOfferDto) {
//        logger.info("selectLoanOffer - Входные данные: {}", loanOfferDto);
//
//        UUID statementId = loanOfferDto.getStatementId();
//        Statement statement = statementRepository.findById(statementId)
//                .orElseThrow(() -> new StatementNotFoundException("Statement not found with id: " + statementId));
//        logger.debug("Заявка найдена: {}", statement);
//
//        // Обновляем статус заявки
//        statement.setStatus(ApplicationStatus.PREAPPROVAL);
//        logger.debug("Статус заявки обновлён на PREAPPROVAL");
//
//        // Обновляем историю статусов
//        List<StatementStatusHistory> history = statement.getStatementStatusHistory() != null
//                ? new ArrayList<>(statement.getStatementStatusHistory())
//                : new ArrayList<>();
//
//        StatementStatusHistory newStatus = new StatementStatusHistory();
//        newStatus.setStatus(ApplicationStatus.APPROVED.name());
//        newStatus.setTime(LocalDateTime.now());
//        newStatus.setChangeType(ChangeType.AUTOMATIC);
//
//        history.add(newStatus);
//        statement.setStatementStatusHistory(history);
//        logger.debug("Добавлена новая запись в историю статусов: {}", newStatus);
//
//        // Устанавливаем принятое предложение
//        LoanOffer appliedOffer = mapToLoanOffer(loanOfferDto);
//        statement.setAppliedOffer(appliedOffer);
//        logger.debug("Установлено принятое предложение: {}", appliedOffer);
//
//        // Сохраняем обновлённую заявку
//        statementRepository.save(statement);
//        logger.info("selectLoanOffer - Заявка обновлена и сохранена: {}", statement);
//    }
//
//    private LoanOffer mapToLoanOffer(LoanOfferDto dto) {
//        LoanOffer loanOffer = new LoanOffer();
//        loanOffer.setStatementId(dto.getStatementId());
//        loanOffer.setRequestedAmount(dto.getRequestedAmount());
//        loanOffer.setTotalAmount(dto.getTotalAmount());
//        loanOffer.setTerm(dto.getTerm());
//        loanOffer.setMonthlyPayment(dto.getMonthlyPayment());
//        loanOffer.setRate(dto.getRate());
//        loanOffer.setIsInsuranceEnabled(dto.getIsInsuranceEnabled());
//        loanOffer.setIsSalaryClient(dto.getIsSalaryClient());
//        return loanOffer;
//    }
//
//    @Transactional
//    public void finishRegistration(String statementIdStr, FinishRegistrationRequestDto finishDto) {
//        logger.info("finishRegistration - Входные данные: statementId={}, finishDto={}", statementIdStr, finishDto);
//
//        UUID statementId = UUID.fromString(statementIdStr);
//
//        // Получаем заявку из БД
//        Statement statement = statementRepository.findById(statementId)
//                .orElseThrow(() -> new StatementNotFoundException("Statement not found with id: " + statementId));
//        logger.debug("Заявка найдена: {}", statement);
//
//        // Создаём ScoringDataDto и насыщаем его из FinishRegistrationRequestDto и Client
//        ScoringDataDto scoringData = mapToScoringDataDto(finishDto, statement.getClient());
//        scoringData.setAmount(statement.getAppliedOffer().getRequestedAmount());
//        scoringData.setTerm(statement.getAppliedOffer().getTerm());
//        scoringData.setIsInsuranceEnabled(statement.getAppliedOffer().getIsInsuranceEnabled());
//        scoringData.setIsSalaryClient(statement.getAppliedOffer().getIsSalaryClient());
//        logger.debug("Создан ScoringDataDto: {}", scoringData);
//
//        // Отправляем POST запрос в микросервис калькулятора
//        ResponseEntity<CreditDto> response = restTemplate.postForEntity(
//                CALCULATOR_CALC_URL,
//                scoringData,
//                CreditDto.class
//        );
//
//        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
//            logger.error("Ошибка при вызове микросервиса калькулятора: статус {}, тело ответа {}", response.getStatusCode(), response.getBody());
//            throw new RuntimeException("Ошибка при вызове микросервиса калькулятора");
//        }
//
//        CreditDto creditDto = response.getBody();
//        logger.debug("Получен CreditDto: {}", creditDto);
//
//        // Создаём сущность Credit из CreditDto
//        Credit credit = mapToCredit(creditDto);
//        logger.debug("Преобразован CreditDto в Credit: {}", credit);
//
//        // Сохраняем Credit в базу
//        creditRepository.save(credit);
//        logger.info("Credit сохранён: {}", credit);
//
//        // Обновляем заявку
//        statement.setCredit(credit);
//        statement.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);
//        logger.debug("Обновлён статус заявки на PREPARE_DOCUMENTS");
//
//        // Обновляем историю статусов
//        List<StatementStatusHistory> history = statement.getStatementStatusHistory() != null
//                ? new ArrayList<>(statement.getStatementStatusHistory())
//                : new ArrayList<>();
//
//        StatementStatusHistory newStatus = new StatementStatusHistory();
//        newStatus.setStatus(ApplicationStatus.PREPARE_DOCUMENTS.name());
//        newStatus.setTime(LocalDateTime.now());
//        newStatus.setChangeType(ChangeType.AUTOMATIC);
//
//        history.add(newStatus);
//        statement.setStatementStatusHistory(history);
//        logger.debug("Добавлена запись в историю статусов: {}", newStatus);
//
//        // Сохраняем обновлённую заявку
//        statementRepository.save(statement);
//        logger.info("finishRegistration - Заявка обновлена и сохранена: {}", statement);
//    }
//
//    private ScoringDataDto mapToScoringDataDto(FinishRegistrationRequestDto finishDto, Client client) {
//        ScoringDataDto scoringData = new ScoringDataDto();
//        scoringData.setFirstName(client.getFirstName());
//        scoringData.setLastName(client.getLastName());
//        scoringData.setMiddleName(client.getMiddleName());
//        scoringData.setGender(finishDto.getGender());
//        scoringData.setBirthdate(client.getBirthDate());
//        scoringData.setPassportSeries(client.getPassport().getSeries());
//        scoringData.setPassportNumber(client.getPassport().getNumber());
//        scoringData.setPassportIssueDate(finishDto.getPassportIssueDate());
//        scoringData.setPassportIssueBranch(finishDto.getPassportIssueBranch());
//        scoringData.setMaritalStatus(finishDto.getMaritalStatus());
//        scoringData.setDependentAmount(finishDto.getDependentAmount());
//        scoringData.setEmployment(finishDto.getEmployment());
//        scoringData.setAccountNumber(finishDto.getAccountNumber());
//
//        return scoringData;
//
//    }
//
//    private Credit mapToCredit(CreditDto creditDto) {
//        Credit credit = new Credit();
//        credit.setCreditId(null);
//        credit.setAmount(creditDto.getAmount());
//        credit.setTerm(creditDto.getTerm());
//        credit.setMonthlyPayment(creditDto.getMonthlyPayment());
//        credit.setRate(creditDto.getRate());
//        credit.setPsk(creditDto.getPsk());
//        List<PaymentSchedule> paymentSchedule = creditDto.getPaymentSchedule().stream()
//                .map(dto -> {
//                    PaymentSchedule element = new PaymentSchedule();
//                    element.setNumber(dto.getNumber());
//                    element.setDate(dto.getDate());
//                    element.setTotalPayment(dto.getTotalPayment());
//                    element.setInterestPayment(dto.getInterestPayment());
//                    element.setDebtPayment(dto.getDebtPayment());
//                    element.setRemainingDebt(dto.getRemainingDebt());
//                    return element;
//                })
//                .collect(Collectors.toList());
//
//        credit.setPaymentSchedule(paymentSchedule);
//        credit.setInsuranceEnabled(creditDto.getIsInsuranceEnabled());
//        credit.setSalaryClient(creditDto.getIsSalaryClient());
//        credit.setCreditStatus(CreditStatus.CALCULATED);
//
//        return credit;
//    }
//}

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
        statement.setStatus(ApplicationStatus.PREAPPROVAL);

        addStatusHistory(statement, ApplicationStatus.APPROVED, ChangeType.AUTOMATIC);

        statement.setAppliedOffer(dealMapper.toLoanOffer(loanOfferDto));
        statementRepository.save(statement);
        logger.info("Заявка обновлена и сохранена: {}", statement);
    }

    @Transactional
    public void finishRegistration(String statementIdStr, FinishRegistrationRequestDto finishDto) {
        logger.info("statementIdStr: {}", statementIdStr);
        logger.info("finishDto: {}", finishDto);
        logger.info("finishRegistration - Входные данные: statementId={}, finishDto={}", statementIdStr, finishDto);
        Statement statement = findStatement(UUID.fromString(statementIdStr));

        ScoringDataDto scoringData = dealMapper.toScoringDataDto(
                finishDto, statement.getClient(), statement.getAppliedOffer());
        logger.debug("Создан ScoringDataDto: {}", scoringData);

        CreditDto creditDto = calculatorRequester.calculateCredit(scoringData);
        logger.debug("Получен CreditDto: {}", creditDto);

        Credit credit = dealMapper.toCredit(creditDto);
        creditRepository.save(credit);
        logger.info("Credit сохранён: {}", credit);

        statement.setCredit(credit);
        statement.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        addStatusHistory(statement, ApplicationStatus.PREPARE_DOCUMENTS, ChangeType.AUTOMATIC);

        statementRepository.save(statement);
        logger.info("finishRegistration - Заявка обновлена и сохранена: {}", statement);
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
        statement.setStatus(ApplicationStatus.DOCUMENT_CREATED);

        Credit credit = new Credit();
        credit.setAmount(requestDto.getAmount());
        credit.setTerm(requestDto.getTerm());
        credit.setCreditStatus(CreditStatus.CALCULATED);
        credit = creditRepository.save(credit);
        statement.setCredit(credit);

        statement.setStatementStatusHistory(
                new ArrayList<>(List.of(createStatusHistory(ApplicationStatus.DOCUMENT_CREATED, now, ChangeType.AUTOMATIC)))
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
                .orElseThrow(() -> new StatementNotFoundException("Statement not found with id: " + statementId));
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


