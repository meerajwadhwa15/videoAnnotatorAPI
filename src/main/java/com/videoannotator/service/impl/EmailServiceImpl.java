package com.videoannotator.service.impl;

import com.videoannotator.model.Mail;
import com.videoannotator.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public void sendMail(Mail mail, String mailType) throws MessagingException {
        var context = new Context();
        context.setVariable("mail", mail);

        String process = templateEngine.process(mailType, context);
        var mimeMessage = javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(mail.getSubject());
        helper.setText(process, true);
        helper.setTo(mail.getSendTo());
        javaMailSender.send(mimeMessage);
    }
}
