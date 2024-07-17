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
import java.util.List;
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
                .creationDate(LocalDateTime.now())
                .statusHistory(new ArrayList<>(Arrays.asList(
                        StatusHistory
                                .builder()
                                .status(ApplicationStatus.PREAPPROVAL)
                                .time(LocalDateTime.now())
                                .changeType(ChangeType.AUTOMATIC)
                                .build())))
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
        updateStatementStatus(statement, ApplicationStatus.APPROVED);
        statement.setAppliedOffer(loanOfferDto);

        log.info("Updated statement: {}", statement);

        kafkaProducer.sendFinishRegistrationMessage(
                EmailMessage.builder()
                        .address(statement.getClient().getEmail())
                        .theme(Theme.FINISH_REGISTRATION)
                        .statementId(loanOfferDto.getStatementId())
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
     * Method gets all Statements from the database
     * @return List of all Statement entities from the database
     */
    @Override
    public List<Statement> getAllStatements() {
        return statementRepository.findAll();
    }


    /**
     * Method updates the Statement status to DOCUMENT_CREATED
     * @param statementId - ID of the Statement entity
     */
    @Transactional
    @Override
    public void updateToDocumentCreated(UUID statementId) {
        Statement statement = getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.PREPARE_DOCUMENTS) {

            updateStatementStatus(statement, ApplicationStatus.DOCUMENT_CREATED);

            log.info("Updated status for Statement with ID [{}]: {}", statement.getStatementId(), statement.getStatus());
        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }
    }


    /**
     * Method updates given Statement entity
     * @param statement - the Statement entity to be updated
     */
    @Override
    public void updateStatement(Statement statement) {
        statementRepository.save(statement);
    }


    /**
     * Method updates the Statement status to the one passed in the arguments
     * @param statement - Statement entity to be updated
     * @param newStatus - new status for the Statement entity
     */
    @Override
    public void updateStatementStatus(Statement statement, ApplicationStatus newStatus) {
        statement.setStatus(newStatus);
        statement.getStatusHistory().add(StatusHistory
                .builder()
                .status(newStatus)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());
    }

}
