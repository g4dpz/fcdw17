package uk.me.g4dpz.fcdwrawdata.service;

import uk.me.g4dpz.fcdwrawdata.messaging.Mail;

import jakarta.mail.MessagingException;
import java.io.IOException;

public interface EmailService {
    void sendRegistrationMessage(Mail mail) throws MessagingException, IOException;
}
