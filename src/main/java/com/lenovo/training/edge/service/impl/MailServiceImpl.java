package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.entity.EmailData;
import com.lenovo.training.edge.service.MailService;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger("MailServiceImpl");
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailData emailData) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            LOGGER.info("Creating mime message");
            mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setTo(emailData.getRecipient());
            mimeMessageHelper.setFrom(emailData.getSender());
            mimeMessageHelper.setSubject(emailData.getSubject());
            mimeMessageHelper.setText(emailData.getMessage());
        } catch (MessagingException e) {
            LOGGER.error("Mail sending error!");
            throw new RuntimeException(e.getMessage());
        }
        LOGGER.info("Mail sent!");
        mailSender.send(message);
    }
}
