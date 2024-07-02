package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.EmailMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendFinishRegistrationMessage(EmailMessage emailMessage) {
        kafkaTemplate.send("finish-registration", emailMessage);
        log.info("Sent message = [{}]", emailMessage);
    }
}
