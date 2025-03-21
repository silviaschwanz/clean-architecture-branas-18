package com.branas.clean_architecture.application;

public interface MailerGateway {
    void send(String recipient, String subject, String message);
}
