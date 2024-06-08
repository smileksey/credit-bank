package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.dto.enums.ChangeType;
import org.smileksey.deal.exceptions.StatementNotFoundException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.models.StatusHistory;
import org.smileksey.deal.repositories.StatementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final StatementRepository statementRepository;


    @Transactional
    @Override
    public Statement createAndSaveStatement(Client client) {

        Statement statement = Statement.builder()
                .statementId(UUID.randomUUID())
                .client(client)
                .creationDate(LocalDate.now())
                .statusHistory(new ArrayList<>())
                .build();

        log.info("Created statement: {}", statement);

        return statementRepository.save(statement);
    }


    @Transactional
    @Override
    public void updateStatementWithSelectedOffer(LoanOfferDto loanOfferDto) {

        Statement statement = getStatementById(loanOfferDto.getStatementId());

        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        statement.getStatusHistory().add(StatusHistory
                .builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());

        statement.setAppliedOffer(loanOfferDto);

        log.info("Updated statement: {}", statement);
    }


    @Override
    public Statement getStatementById(UUID statementId) {

        return statementRepository.findById(statementId)
                .orElseThrow(() -> new StatementNotFoundException("Statement with ID " + statementId + " was NOT found"));
    }

}
