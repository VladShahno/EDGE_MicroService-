package com.lenovo.training.edge.service;

import com.lenovo.training.edge.entity.EmailData;

public interface MailService {

    void sendEmail(EmailData emailData);
}
