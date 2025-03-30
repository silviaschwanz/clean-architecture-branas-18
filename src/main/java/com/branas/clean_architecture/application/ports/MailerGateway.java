package com.branas.clean_architecture.application.ports;

public interface MailerGateway {
    void send(String recipient, String subject, String message);
}
