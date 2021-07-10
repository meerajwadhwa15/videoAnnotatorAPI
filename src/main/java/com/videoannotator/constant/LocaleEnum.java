package com.videoannotator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocaleEnum {
    VN("vi"),
    EN("en");
    private final String value;
}
