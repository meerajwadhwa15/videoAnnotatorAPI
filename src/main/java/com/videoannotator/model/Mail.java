package com.videoannotator.model;

import lombok.Data;

@Data
public class Mail {
    private String sendTo;
    private String sendForm;
    private String subject;
    private String url;
}
