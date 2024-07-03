package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.EmailMessage;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.dto.enums.ChangeType;
import org.smileksey.deal.dto.enums.Theme;
import org.smileksey.deal.exceptions.InvalidSesCodeException;
import org.smileksey.deal.exceptions.StatementStatusException;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.models.StatusHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentsService {

    private final StatementService statementService;
    private final KafkaProducer kafkaProducer;


    @Transactional
    public void handleSendDocuments(UUID statementId) {
        Statement statement = statementService.getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.CC_APPROVED) {
            kafkaProducer.sendSendDocumentsMessage(
                    EmailMessage.builder()
                            .address(statement.getClient().getEmail())
                            .theme(Theme.SEND_DOCUMENTS)
                            .statementId(statementId.getMostSignificantBits())
                            .build());

            statement.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);
            statement.getStatusHistory().add(StatusHistory
                    .builder()
                    .status(ApplicationStatus.PREPARE_DOCUMENTS)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build());

            log.info("Updated statement: {}", statement);

        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }
    }


    @Transactional
    public void handleSignDocuments(UUID statementId) {
        Statement statement = statementService.getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.DOCUMENT_CREATED) {
            kafkaProducer.sendSendSESMessage(
                    EmailMessage.builder()
                            .address(statement.getClient().getEmail())
                            .theme(Theme.SEND_SES)
                            .statementId(statementId.getMostSignificantBits())
                            .build());

            //FIXME create normal code generation and new DTO to send code via Kafka
            statement.setSesCode("java");
            log.info("Generated SES code for Statement with ID [{}]: {}", statement.getStatementId(), statement.getSesCode());

        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }
    }


    @Transactional
    public void handleVerifySESCode(UUID statementId, String sesCode) {
        Statement statement = statementService.getStatementById(statementId);

        if (statement.getStatus() == ApplicationStatus.DOCUMENT_CREATED) {
            if(statement.getSesCode().equals(sesCode.trim())) {
                kafkaProducer.sendCreditIssuedMessage(
                        EmailMessage.builder()
                                .address(statement.getClient().getEmail())
                                .theme(Theme.CREDIT_ISSUED)
                                .statementId(statementId.getMostSignificantBits())
                                .build());

                statement.setStatus(ApplicationStatus.CREDIT_ISSUED);
                statement.getStatusHistory().add(StatusHistory
                        .builder()
                        .status(ApplicationStatus.DOCUMENT_SIGNED)
                        .time(LocalDateTime.now())
                        .changeType(ChangeType.AUTOMATIC)
                        .build());
                statement.getStatusHistory().add(StatusHistory
                        .builder()
                        .status(ApplicationStatus.CREDIT_ISSUED)
                        .time(LocalDateTime.now())
                        .changeType(ChangeType.AUTOMATIC)
                        .build());

                log.info("Updated statement: {}", statement);
            } else {
                throw new InvalidSesCodeException("Invalid SesCode: " + sesCode);
            }
        } else {
            throw new StatementStatusException("Statement has inappropriate status for this action: [" + statement.getStatus() + "]");
        }
    }

}
