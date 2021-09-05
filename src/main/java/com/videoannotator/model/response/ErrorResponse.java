package com.videoannotator.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> error = new LinkedList<>();
}
