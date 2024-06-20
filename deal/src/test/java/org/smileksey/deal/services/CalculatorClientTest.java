package org.smileksey.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.deal.dto.CreditDto;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.dto.ScoringDataDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CalculatorClient calculatorClient;

    @Test
    void getCreditDtoResponse() {

        lenient().when(restTemplate.exchange(
                        anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        (ParameterizedTypeReference<Object>) any()))
                .thenReturn(new ResponseEntity<>(new CreditDto(), HttpStatus.OK));

        ResponseEntity<CreditDto> creditDtoResponse = calculatorClient.getCreditDtoResponse(ScoringDataDto.builder().build());

        assertNotNull(creditDtoResponse);
        assertEquals(HttpStatus.OK, creditDtoResponse.getStatusCode());
        assertTrue(creditDtoResponse.hasBody());

        verify(restTemplate, times(1)).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Object>) any());
    }

    @Test
    void getLoanOffersResponse() {

        lenient().when(restTemplate.exchange(
                        anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        (ParameterizedTypeReference<Object>) any()))
                .thenReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));

        ResponseEntity<List<LoanOfferDto>> loanOffersResponse = calculatorClient.getLoanOffersResponse(new LoanStatementRequestDto());

        assertNotNull(loanOffersResponse);
        assertEquals(HttpStatus.OK, loanOffersResponse.getStatusCode());
        assertTrue(loanOffersResponse.hasBody());

        verify(restTemplate, times(1)).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Object>) any());
    }


}