package org.smileksey.dossier.services;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
