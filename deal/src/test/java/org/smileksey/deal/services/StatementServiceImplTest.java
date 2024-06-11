package org.smileksey.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.dto.enums.ChangeType;
import org.smileksey.deal.exceptions.StatementNotFoundException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.repositories.StatementRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceImplTest {

    @Mock
    private StatementRepository statementRepository;

    @InjectMocks
    private StatementServiceImpl statementServiceImpl;


    @Test
    void createAndSaveStatementShouldReturnFilledObject() {

        Client client = new Client();
        client.setClientId(UUID.randomUUID());

        Statement mockStatement = Statement.builder()
                .statementId(UUID.randomUUID())
                .client(client)
                .creationDate(LocalDate.now())
                .statusHistory(new ArrayList<>())
                .build();

        when(statementRepository.save(any())).thenReturn(mockStatement);

        Statement savedStatement = statementServiceImpl.createAndSaveStatement(client);

        assertNotNull(savedStatement);

        verify(statementRepository, times(1)).save(any());

        assertEquals(mockStatement.getStatementId(), savedStatement.getStatementId());
        assertEquals(mockStatement.getClient().getClientId(), savedStatement.getClient().getClientId());
        assertEquals(mockStatement.getCreationDate(), savedStatement.getCreationDate());
    }


    @Test
    void updateStatementWithSelectedOffer() {

        when(statementRepository.findById(any())).thenReturn(Optional.of(Statement.builder()
                .statusHistory(new ArrayList<>())
                .build()));

        LoanOfferDto loanOfferDto = new LoanOfferDto();

        Statement updatedStatement = statementServiceImpl.updateStatementWithSelectedOffer(loanOfferDto);

        verify(statementRepository, times(1)).findById(any());
        assertNotNull(updatedStatement);
        assertEquals(ApplicationStatus.PREAPPROVAL, updatedStatement.getStatus());
        assertEquals(ApplicationStatus.PREAPPROVAL, updatedStatement.getStatusHistory().get(0).getStatus());
        assertEquals(LocalDateTime.now().toLocalDate(), updatedStatement.getStatusHistory().get(0).getTime().toLocalDate());
        assertEquals(ChangeType.AUTOMATIC, updatedStatement.getStatusHistory().get(0).getChangeType());
        assertEquals(loanOfferDto, updatedStatement.getAppliedOffer());
    }


    @Test
    void getStatementByIdShouldReturnStatement() {

        when(statementRepository.findById(any())).thenReturn(Optional.of(new Statement()));

        assertNotNull(statementServiceImpl.getStatementById(UUID.randomUUID()));
    }


    @Test
    void getStatementByIdShouldThrowExceptionWhenStatementDoesNotExist() {

        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(StatementNotFoundException.class, () -> statementServiceImpl.getStatementById(UUID.randomUUID()));
    }
}