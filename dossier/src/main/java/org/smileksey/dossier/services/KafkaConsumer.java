package org.smileksey.dossier.services;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.dossier.dto.EmailMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "finish-registration", groupId = "my_group")
    public void listenFinishRegistration(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [finish-registration]", emailMessage);
    }

    @KafkaListener(topics = "create-documents", groupId = "my_group")
    public void listenCreateDocuments(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [create-documents]", emailMessage);
    }

    @KafkaListener(topics = "send-documents", groupId = "my_group")
    public void listenSendDocuments(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [send-documents]", emailMessage);
    }

    @KafkaListener(topics = "send-ses", groupId = "my_group")
    public void listenSendSES(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [send-ses]", emailMessage);
    }

    @KafkaListener(topics = "credit-issued", groupId = "my_group")
    public void listenCreditIssued(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [credit-issued]", emailMessage);
    }

}
