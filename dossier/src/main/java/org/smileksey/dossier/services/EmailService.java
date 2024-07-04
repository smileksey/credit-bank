package org.smileksey.dossier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;

    /** Address to send email from */
    @Value("${spring.mail.username}")
    private String from;

    /** Address to send email to */
    @Value("${mail.recipient}")
    private String to;

    @Async
    public void sendEmail(String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
        log.info("Sent email to {} with subject [{}]", to, subject);
    }
}
