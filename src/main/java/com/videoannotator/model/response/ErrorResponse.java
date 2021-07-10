package com.videoannotator.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> error = new LinkedList<>();
}
