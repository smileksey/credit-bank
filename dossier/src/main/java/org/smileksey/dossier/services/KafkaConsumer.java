package org.smileksey.dossier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.dossier.dto.EmailMessage;
import org.smileksey.dossier.dto.EmailMessageWithSES;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final EmailService emailService;
    private final DealClient dealClient;


    /**
     * Method receives messages from Kafka's 'finish-registration' topic
     * and sends a corresponding email to the client
     * @param emailMessage - object to be extracted from the message
     */
    @KafkaListener(topics = "finish-registration", groupId = "my_group")
    public void listenFinishRegistration(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [finish-registration]", emailMessage);

        emailService.sendEmail(
                emailMessage.getAddress(),
                emailMessage.getTheme().toString(),
                "Ваша заявка предварительно одобрена, завершите оформление.");
    }


    /**
     * Method receives messages from Kafka's 'create-documents' topic
     * and sends a corresponding email to the client
     * @param emailMessage - object to be extracted from the message
     */
    @KafkaListener(topics = "create-documents", groupId = "my_group")
    public void listenCreateDocuments(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [create-documents]", emailMessage);

        emailService.sendEmail(
                emailMessage.getAddress(),
                emailMessage.getTheme().toString(),
                "Заявка одобрена. Можно отправить запрос на формирование документов.");
    }


    /**
     * Method receives messages from Kafka's 'send-documents' topic,
     * sends a request to 'deal' microservice
     * and sends a corresponding email to the client
     * @param emailMessage - object to be extracted from the message
     */
    @KafkaListener(topics = "send-documents", groupId = "my_group")
    public void listenSendDocuments(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [send-documents]", emailMessage);

        dealClient.sendUpdateStatementStatus(emailMessage.getStatementId());
        emailService.sendEmail(
                emailMessage.getAddress(),
                emailMessage.getTheme().toString(),
                "Ваши документы готовы. Для подписания отправьте запрос на получение ПЭП.");
    }


    /**
     * Method receives messages from Kafka's 'send-ses' topic
     * and sends a corresponding email to the client
     * @param emailMessage - object to be extracted from the message
     */
    @KafkaListener(topics = "send-ses", groupId = "my_group", containerFactory = "kafkaListenerContainerFactoryForSES")
    public void listenSendSES(EmailMessageWithSES emailMessage) {
        log.info("Received message: [{}] | topic = [send-ses]", emailMessage);

        emailService.sendEmail(
                emailMessage.getAddress(),
                emailMessage.getTheme().toString(),
                "Ваш код ПЭП: " + emailMessage.getSesCode());
    }


    /**
     * Method receives messages from Kafka's 'credit-issued' topic
     * and sends a corresponding email to the client
     * @param emailMessage - object to be extracted from the message
     */
    @KafkaListener(topics = "credit-issued", groupId = "my_group")
    public void listenCreditIssued(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [credit-issued]", emailMessage);

        emailService.sendEmail(
                emailMessage.getAddress(),
                emailMessage.getTheme().toString(),
                "Кредит оформлен.");
    }


    /**
     * Method receives messages from Kafka's 'statement-denied' topic
     * and sends a corresponding email to the client
     * @param emailMessage - object to be extracted from the message
     */
    @KafkaListener(topics = "statement-denied", groupId = "my_group")
    public void listenStatementDenied(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [statement-denied]", emailMessage);

        emailService.sendEmail(
                emailMessage.getAddress(),
                emailMessage.getTheme().toString(),
                "В кредите отказано.");
    }

}
