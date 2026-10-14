package com.idaav8.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordEmail(
            String to,
            String name,
            String password) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(
                "IDAA Password Reset");

        message.setText(
                "Hello " + name + ",\n\n" +
                "Your password has been reset.\n\n" +
                "Password: " + password +
                "\n\nPlease login and change it if required.");

        mailSender.send(message);
    }
}