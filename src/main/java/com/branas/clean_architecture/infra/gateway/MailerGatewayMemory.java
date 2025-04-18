package com.branas.clean_architecture.infra.gateway;

import com.branas.clean_architecture.application.ports.MailerGateway;
import org.springframework.stereotype.Service;

@Service
public class MailerGatewayMemory implements MailerGateway {

    @Override
    public void send(String recipient, String subject, String message) {
        System.out.println("Recipient: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
    }

}

