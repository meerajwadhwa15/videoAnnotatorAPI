package com.videoannotator.service;

import com.videoannotator.model.Mail;

import javax.mail.MessagingException;

/**
 * Service to send email for confirm or reset pass word
 */
public interface IEmailService {
    /**
     * Register User ( store ) and returns the user object
     *
     * @param mail - Mail information
     */
    void sendResetPasswordMail(Mail mail) throws MessagingException;
}
