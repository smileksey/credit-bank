package org.smileksey.dossier.services;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.dossier.dto.EmailMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "finish-registration", groupId = "my_group")
    public void listen(EmailMessage emailMessage) {
        log.info("Received message: [{}]", emailMessage);
    }
}
