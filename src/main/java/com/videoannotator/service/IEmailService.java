package com.videoannotator.service;

import com.videoannotator.model.Mail;

import javax.mail.MessagingException;

/**
 * Service to send email for confirm or reset pass word
 */
public interface IEmailService {
    /**
     * Confirm email or reset password
     *
     * @param mail - Mail information
     */
    void sendMail(Mail mail, String mailType) throws MessagingException;
}
