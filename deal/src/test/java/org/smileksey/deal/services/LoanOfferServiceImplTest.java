package org.smileksey.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.exceptions.InvalidMSResponseException;
import org.smileksey.deal.models.Statement;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceImplTest {

    @Mock
    private ClientServiceImpl clientServiceImpl;

    @Mock
    private StatementServiceImpl statementServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LoanOfferServiceImpl loanOfferService;



    @Test
    void getLoanOffersShouldReturnLoanOffersListWhenCCResponseIsValid() {

        List<LoanOfferDto> loanOffers = Arrays.asList(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());

        when(statementServiceImpl.createAndSaveStatement(any())).thenReturn(new Statement());

        lenient().when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Object>) any()))
                .thenReturn(new ResponseEntity<>(loanOffers, HttpStatus.OK));

        List<LoanOfferDto> returnedLoanOffers = loanOfferService.getLoanOffers(new LoanStatementRequestDto());

        assertNotNull(returnedLoanOffers);
        assertEquals(loanOffers.size(), returnedLoanOffers.size());

        verify(restTemplate, times(1)).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Object>) any());

        verify(clientServiceImpl, times(1)).createAndSaveClient(any());
        verify(statementServiceImpl, times(1)).createAndSaveStatement(any());
    }

    @Test
    void getLoanOffersWhenCCResponseIsInvalid() {

        lenient().when(restTemplate.exchange(
                        anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        (ParameterizedTypeReference<Object>) any()))
                .thenReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));

        assertThrows(InvalidMSResponseException.class, () -> loanOfferService.getLoanOffers(new LoanStatementRequestDto()));

        verify(restTemplate, times(1)).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                (ParameterizedTypeReference<Object>) any());
    }
}