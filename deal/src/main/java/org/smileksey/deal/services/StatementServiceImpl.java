package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.EmailMessage;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.dto.enums.ChangeType;
import org.smileksey.deal.dto.enums.Theme;
import org.smileksey.deal.exceptions.StatementNotFoundException;
import org.smileksey.deal.exceptions.StatementStatusException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.models.StatusHistory;
import org.smileksey.deal.repositories.StatementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final StatementRepository statementRepository;
    private final KafkaProducer kafkaProducer;


    /**
     * Method creates a new Statement entity associated with the Client and saves it to the database
     * @param client - associated Client entity
     * @return Statement entity saved to the database
     */
    @Transactional
    @Override
    public Statement createAndSaveStatement(Client client) {

        Statement statement = Statement.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .statusHistory(new ArrayList<>(Arrays.asList(
                        StatusHistory
                                .builder()
                                .status(ApplicationStatus.PREAPPROVAL)
                                .time(LocalDateTime.now())
                                .changeType(ChangeType.AUTOMATIC)
                                .build()
                )))
                .build();

        log.info("Created statement: {}", statement);

        return statementRepository.save(statement);
    }


    /**
     * Method updates the Statement entity data with a chosen LoanOfferDto data
     *
     * @param loanOfferDto - loan offer chosen by the client
     * @return updated Statement
     */
    @Transactional
    @Override
    public Statement updateStatementWithSelectedOffer(LoanOfferDto loanOfferDto) {

        Statement statement = getStatementById(loanOfferDto.getStatementId());

        statement.setStatus(ApplicationStatus.APPROVED);
        statement.getStatusHistory().add(StatusHistory
                .builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());

        statement.setAppliedOffer(loanOfferDto);

        log.info("Updated statement: {}", statement);

        kafkaProducer.sendFinishRegistrationMessage(
                EmailMessage.builder()
                        .address(statement.getClient().getEmail())
                        .theme(Theme.FINISH_REGISTRATION)
                        .statementId(loanOfferDto.getStatementId().getMostSignificantBits())
                        .build());

        return statement;
    }


    /**
     * Method gets the Statement entity from the database by its ID
     * @param statementId - ID of the Statement entity
     * @return Statement entity with required ID else throws StatementNotFoundException
     */
    @Override
    public Statement getStatementById(UUID statementId) {

        return statementRepository.findById(statementId)
                .orElseThrow(() -> new StatementNotFoundException("Statement with ID " + statementId + " was NOT found"));
    }


    /**
     * Method updates the Statement status
     * @param statementId - ID of the Statement entity
     */
    @Transactional
    @Override
    public void updateStatementStatus(UUID statementId) {
        Statement statement = getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.PREPARE_DOCUMENTS) {
            statement.setStatus(ApplicationStatus.DOCUMENT_CREATED);
            log.info("Updated status for Statement with ID [{}]: {}", statement.getStatementId(), statement.getStatus());
        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }

    }

}
