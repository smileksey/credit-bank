package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.EmailMessage;
import org.smileksey.deal.dto.EmailMessageWithSES;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendFinishRegistrationMessage(EmailMessage emailMessage) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("finish-registration", emailMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message = [{}] | topic = [finish-registration]", emailMessage);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message = [{}] | topic = [finish-registration]", emailMessage);
            }
        });
    }


    public void sendCreateDocumentsMessage(EmailMessage emailMessage) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("create-documents", emailMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message = [{}] | topic = [create-documents]", emailMessage);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message = [{}] | topic = [create-documents]", emailMessage);
            }
        });
    }


    public void sendSendDocumentsMessage(EmailMessage emailMessage) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("send-documents", emailMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message = [{}] | topic = [send-documents]", emailMessage);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message = [{}] | topic = [send-documents]", emailMessage);
            }
        });

    }


    public void sendSendSESMessage(EmailMessageWithSES emailMessage) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("send-ses", emailMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message = [{}] | topic = [send-ses]", emailMessage);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message = [{}] | topic = [send-ses]", emailMessage);
            }
        });

    }


    public void sendCreditIssuedMessage(EmailMessage emailMessage) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("credit-issued", emailMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message = [{}] | topic = [credit-issued]", emailMessage);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message = [{}] | topic = [credit-issued]", emailMessage);
            }
        });
    }


    public void sendStatementDeniedMessage(EmailMessage emailMessage) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("statement-denied", emailMessage);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message = [{}] | topic = [statement-denied]", emailMessage);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message = [{}] | topic = [statement-denied]", emailMessage);
            }
        });

    }
}
