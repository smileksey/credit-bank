package org.smileksey.statement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.statement.dto.LoanOfferDto;
import org.smileksey.statement.dto.LoanStatementRequestDto;
import org.smileksey.statement.exceptions.InvalidMSResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceImplTest {

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private LoanOfferServiceImpl loanOfferService;

    @Test
    void getLoanOffersShouldReturnLoanOffersListWhenCCResponseIsValid() {

        List<LoanOfferDto> loanOffers = Arrays.asList(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());

        when(dealClient.getLoanOffersResponse(any())).thenReturn(new ResponseEntity<>(loanOffers, HttpStatus.OK));

        List<LoanOfferDto> returnedLoanOffers = loanOfferService.getLoanOffers(new LoanStatementRequestDto());

        verify(dealClient, times(1)).getLoanOffersResponse(any());

        assertNotNull(returnedLoanOffers);
        assertEquals(loanOffers.size(), returnedLoanOffers.size());
    }


    @Test
    void getLoanOffersWhenCCResponseIsInvalid() {

        when(dealClient.getLoanOffersResponse(any())).thenReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));

        assertThrows(InvalidMSResponseException.class, () -> loanOfferService.getLoanOffers(new LoanStatementRequestDto()));
    }


    @Test
    void selectOffer() {

        when(dealClient.sendSelectedOffer(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        loanOfferService.selectOffer(new LoanOfferDto());

        verify(dealClient, times(1)).sendSelectedOffer(any());
    }
}