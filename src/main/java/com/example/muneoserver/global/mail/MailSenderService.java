package com.example.muneoserver.global.mail;

public interface MailSenderService {

    void send(String to, String subject, String body);
}
