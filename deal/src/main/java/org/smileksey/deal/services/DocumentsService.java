package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.EmailMessage;
import org.smileksey.deal.dto.EmailMessageWithSES;
import org.smileksey.deal.dto.SESCodeDto;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.dto.enums.Theme;
import org.smileksey.deal.exceptions.InvalidSesCodeException;
import org.smileksey.deal.exceptions.StatementStatusException;
import org.smileksey.deal.models.Statement;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentsService {

    private final StatementService statementService;
    private final KafkaProducer kafkaProducer;


    /**
     * Method updates corresponding Statement entity with a new status
     * when 'send documents' request is received from client
     * @param statementId - ID of the Statement entity
     */
    public void handleSendDocuments(UUID statementId) {
        Statement statement = statementService.getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.CC_APPROVED) {

            statementService.updateStatementStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS);
            statementService.updateStatement(statement);

            kafkaProducer.sendSendDocumentsMessage(
                    EmailMessage.builder()
                            .address(statement.getClient().getEmail())
                            .theme(Theme.SEND_DOCUMENTS)
                            .statementId(statementId)
                            .build());

            log.info("Updated statement: {}", statement);

        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }
    }


    /**
     * Method updates corresponding Statement entity with a generated SES code
     * when 'sign documents' request is received from client
     * @param statementId - ID of the Statement entity
     */
    public void handleSignDocuments(UUID statementId) {
        Statement statement = statementService.getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.DOCUMENT_CREATED) {
            String sesCode = UUID.randomUUID().toString();

            statement.setSesCode(sesCode);
            statementService.updateStatement(statement);

            kafkaProducer.sendSendSESMessage(
                    EmailMessageWithSES.builder()
                            .address(statement.getClient().getEmail())
                            .theme(Theme.SEND_SES)
                            .statementId(statementId)
                            .sesCode(sesCode)
                            .build());

            log.info("Generated SES code for Statement with ID [{}]: {}", statement.getStatementId(), statement.getSesCode());

        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }
    }


    /**
     * Method verifies the SES code received from client
     * and updates corresponding Statement entity with a new status and sign date
     * @param statementId - ID of the Statement entity
     * @param sesCodeDto - dto containing the SES code
     */
    public void handleVerifySESCode(UUID statementId, SESCodeDto sesCodeDto) {
        Statement statement = statementService.getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.DOCUMENT_CREATED) {
            if(statement.getSesCode().equals(sesCodeDto.getSesCode().trim())) {

                statementService.updateStatementStatus(statement, ApplicationStatus.DOCUMENT_SIGNED);
                statementService.updateStatementStatus(statement, ApplicationStatus.CREDIT_ISSUED);
                statement.setSignDate(LocalDateTime.now());
                statementService.updateStatement(statement);

                kafkaProducer.sendCreditIssuedMessage(
                        EmailMessage.builder()
                                .address(statement.getClient().getEmail())
                                .theme(Theme.CREDIT_ISSUED)
                                .statementId(statementId)
                                .build());

                log.info("Updated statement: {}", statement);
            } else {
                throw new InvalidSesCodeException("Invalid SesCode: " + sesCodeDto.getSesCode());
            }
        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }
    }

}
