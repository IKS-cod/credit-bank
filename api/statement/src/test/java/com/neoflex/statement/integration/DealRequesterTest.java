package com.neoflex.statement.integration;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealRequesterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DealRequester dealRequester;

    @BeforeEach
    void setupUrls() throws Exception {

        var dealOffersUrlField = DealRequester.class.getDeclaredField("dealOffersUrl");
        dealOffersUrlField.setAccessible(true);
        dealOffersUrlField.set(dealRequester, "http://mocked.offer.url");

        var dealSelectLoanOfferUrlField = DealRequester.class.getDeclaredField("dealSelectLoanOfferUrl");
        dealSelectLoanOfferUrlField.setAccessible(true);
        dealSelectLoanOfferUrlField.set(dealRequester, "http://mocked.select.url");
    }

    @Test
    void getLoanOffers_ShouldReturnList_WhenResponseIsSuccessful() {
        // given
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        LoanOfferDto[] mockResponseBody = new LoanOfferDto[]{
                new LoanOfferDto() {{
                    setStatementId(UUID.randomUUID());
                    setRequestedAmount(new BigDecimal("10000"));
                    setTerm(12);
                }},
                new LoanOfferDto() {{
                    setStatementId(UUID.randomUUID());
                    setRequestedAmount(new BigDecimal("20000"));
                    setTerm(24);
                }}
        };
        ResponseEntity<LoanOfferDto[]> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(restTemplate.postForEntity(eq("http://mocked.offer.url"), eq(requestDto), eq(LoanOfferDto[].class)))
                .thenReturn(mockResponse);

        // when
        List<LoanOfferDto> offers = dealRequester.getLoanOffers(requestDto);

        // then
        assertThat(offers).hasSize(2);
        assertThat(offers).containsExactlyElementsOf(Arrays.asList(mockResponseBody));
        verify(restTemplate).postForEntity("http://mocked.offer.url", requestDto, LoanOfferDto[].class);
    }

    @Test
    void getLoanOffers_ShouldThrow_WhenResponseIsNotSuccessful() {
        // given
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        ResponseEntity<LoanOfferDto[]> mockResponse = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        when(restTemplate.postForEntity(eq("http://mocked.offer.url"), eq(requestDto), eq(LoanOfferDto[].class)))
                .thenReturn(mockResponse);

        // when - then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dealRequester.getLoanOffers(requestDto));

        assertThat(exception).hasMessageContaining("Ошибка при вызове микросервиса калькулятора");
        verify(restTemplate).postForEntity("http://mocked.offer.url", requestDto, LoanOfferDto[].class);
    }

    @Test
    void selectLoanOffer_ShouldComplete_WhenResponseIsSuccessful() {
        // given
        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());

        ResponseEntity<Void> mockResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.postForEntity(eq("http://mocked.select.url"), eq(offerDto), eq(Void.class)))
                .thenReturn(mockResponse);

        // when
        dealRequester.selectLoanOffer(offerDto);

        // then
        verify(restTemplate).postForEntity("http://mocked.select.url", offerDto, Void.class);
    }

    @Test
    void selectLoanOffer_ShouldThrow_WhenResponseIsNotSuccessful() {
        // given
        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());

        ResponseEntity<Void> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(restTemplate.postForEntity(eq("http://mocked.select.url"), eq(offerDto), eq(Void.class)))
                .thenReturn(mockResponse);

        // when - then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dealRequester.selectLoanOffer(offerDto));

        assertThat(exception).hasMessageContaining("Ошибка при отправке выбранного предложения в микросервис deal");
        verify(restTemplate).postForEntity("http://mocked.select.url", offerDto, Void.class);
    }
}
