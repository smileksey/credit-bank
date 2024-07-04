package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.EmailMessage;
import org.smileksey.deal.dto.EmailMessageWithSES;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    //TODO add checking the result of sending
    public void sendFinishRegistrationMessage(EmailMessage emailMessage) {
        kafkaTemplate.send("finish-registration", emailMessage);
        log.info("Sent message = [{}] | topic = [finish-registration]", emailMessage);
    }

    public void sendCreateDocumentsMessage(EmailMessage emailMessage) {
        kafkaTemplate.send("create-documents", emailMessage);
        log.info("Sent message = [{}] | topic = [create-documents]", emailMessage);
    }

    public void sendSendDocumentsMessage(EmailMessage emailMessage) {
        kafkaTemplate.send("send-documents", emailMessage);
        log.info("Sent message = [{}] | topic = [send-documents]", emailMessage);
    }

    public void sendSendSESMessage(EmailMessageWithSES emailMessage) {
        kafkaTemplate.send("send-ses", emailMessage);
        log.info("Sent message = [{}] | topic = [send-ses]", emailMessage);
    }

    public void sendCreditIssuedMessage(EmailMessage emailMessage) {
        kafkaTemplate.send("credit-issued", emailMessage);
        log.info("Sent message = [{}] | topic = [credit-issued]", emailMessage);
    }

    public void sendStatementDeniedMessage(EmailMessage emailMessage) {
        kafkaTemplate.send("statement-denied", emailMessage);
        log.info("Sent message = [{}] | topic = [statement-denied]", emailMessage);
    }
}
