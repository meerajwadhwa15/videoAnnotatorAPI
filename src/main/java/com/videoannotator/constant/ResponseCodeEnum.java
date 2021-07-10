package com.videoannotator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {
    OK("200"),
    NOK("400");
    private final String value;
}
