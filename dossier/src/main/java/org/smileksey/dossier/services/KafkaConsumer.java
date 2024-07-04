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

    @KafkaListener(topics = "finish-registration", groupId = "my_group")
    public void listenFinishRegistration(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [finish-registration]", emailMessage);

        emailService.sendEmail(emailMessage.getTheme().toString(), "Ваша заявка предварительно одобрена, завершите оформление.");
    }

    @KafkaListener(topics = "create-documents", groupId = "my_group")
    public void listenCreateDocuments(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [create-documents]", emailMessage);

        emailService.sendEmail(emailMessage.getTheme().toString(), "Заявка одобрена. Можно отправить запрос на формирование документов.");
    }

    //TODO develop sending PUT request to 'deal' to update Statement status
    @KafkaListener(topics = "send-documents", groupId = "my_group")
    public void listenSendDocuments(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [send-documents]", emailMessage);

        emailService.sendEmail(emailMessage.getTheme().toString(), "Ваши документы готовы. Для подписания отправьте запрос на получение ПЭП.");
    }

    @KafkaListener(topics = "send-ses", groupId = "my_group", containerFactory = "kafkaListenerContainerFactoryForSES")
    public void listenSendSES(EmailMessageWithSES emailMessage) {
        log.info("Received message: [{}] | topic = [send-ses]", emailMessage);

        emailService.sendEmail(emailMessage.getTheme().toString(), "Ваш код ПЭП: " + emailMessage.getSesCode());
    }

    @KafkaListener(topics = "credit-issued", groupId = "my_group")
    public void listenCreditIssued(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [credit-issued]", emailMessage);

        emailService.sendEmail(emailMessage.getTheme().toString(), "Кредит оформлен.");
    }

    @KafkaListener(topics = "statement-denied", groupId = "my_group")
    public void listenStatementDenied(EmailMessage emailMessage) {
        log.info("Received message: [{}] | topic = [statement-denied]", emailMessage);

        emailService.sendEmail(emailMessage.getTheme().toString(), "В кредите отказано.");
    }

}
