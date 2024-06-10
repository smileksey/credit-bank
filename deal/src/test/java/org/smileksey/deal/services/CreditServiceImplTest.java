package org.smileksey.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.deal.repositories.CreditRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    private StatementService statementService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CreditServiceImpl creditServiceImpl;



    @Test
    void calculateCreditAndFinishRegistration() {
    }
}