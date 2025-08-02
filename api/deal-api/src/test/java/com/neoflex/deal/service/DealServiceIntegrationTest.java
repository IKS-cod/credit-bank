package com.neoflex.deal.service;

import com.neoflex.deal.DealApplication;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.enums.ApplicationStatus;
import com.neoflex.deal.integration.CalculatorRequester;
import com.neoflex.deal.mapper.DealMapper;
import com.neoflex.deal.model.Client;
import com.neoflex.deal.model.Credit;
import com.neoflex.deal.model.Statement;
import com.neoflex.deal.repository.ClientRepository;
import com.neoflex.deal.repository.CreditRepository;
import com.neoflex.deal.repository.StatementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DealApplication.class)
public class DealServiceIntegrationTest {
    @Autowired
    private DealService dealService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private CalculatorRequester calculatorRequester;
    @Autowired
    private DealMapper dealMapper;
    @Autowired
    private StatementRepository statementRepository;

    @Test
    public void testConcurrentSelectLoanOffer() throws InterruptedException {
        // Подготовим объект
        Client client = new Client();
        client.setClientId(null);
        client.setFirstName("Test");
        client.setLastName("Test");
        client = clientRepository.save(client);
        Credit credit = new Credit();
        credit.setAmount(BigDecimal.valueOf(50000));
        credit.setTerm(10);
        credit = creditRepository.save(credit);

        Statement statement = new Statement();
        statement.setStatementId(null);
        statement.setStatus(ApplicationStatus.APPROVED);
        statement.setCredit(credit);
        statement.setClient(client);
        statement = statementRepository.save(statement);

        LoanOfferDto offer1 = new LoanOfferDto();
        offer1.setStatementId(statement.getStatementId());
        offer1.setRate(BigDecimal.valueOf(10));
        offer1.setTerm(10);
        offer1.setRequestedAmount(BigDecimal.valueOf(50000));
        offer1.setMonthlyPayment(BigDecimal.valueOf(3000));
        offer1.setTotalAmount(BigDecimal.valueOf(70000));
        offer1.setIsSalaryClient(true);
        offer1.setIsInsuranceEnabled(true);

        LoanOfferDto offer2 = new LoanOfferDto();
        offer2.setStatementId(statement.getStatementId());
        offer2.setRate(BigDecimal.valueOf(20));
        offer2.setTerm(20);
        offer2.setRequestedAmount(BigDecimal.valueOf(100000));
        offer2.setMonthlyPayment(BigDecimal.valueOf(5000));
        offer2.setTotalAmount(BigDecimal.valueOf(150000));
        offer2.setIsSalaryClient(false);
        offer2.setIsInsuranceEnabled(false);

        CountDownLatch latchFirstStarted = new CountDownLatch(1);
        CountDownLatch latchContinueFirst = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            // Начали транзакцию
            latchFirstStarted.countDown();
            dealService.selectLoanOffer(offer1);
            // Дожидаемся, чтобы дать время второму ждать
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            latchContinueFirst.countDown();
        });

        Thread thread2 = new Thread(() -> {
            try {
                // Ждем старта первого
                latchFirstStarted.await();

                long startWait = System.currentTimeMillis();
                dealService.selectLoanOffer(offer2);
                long elapsed = System.currentTimeMillis() - startWait;

                System.out.println("Thread 2 waited ms: " + elapsed);
                // Проверяем, что ждал хотя бы 500 мс (примерный порог)
                assertTrue(elapsed >= 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        Thread.sleep(100);
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
