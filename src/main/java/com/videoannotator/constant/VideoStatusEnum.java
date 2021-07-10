package com.videoannotator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VideoStatusEnum {
    ASSIGNED("ASSIGNED"),
    NOT_ASSIGNED("NOT_ASSIGNED"),
    WAIT_ANNOTATION("WAIT_ANNOTATION"),
    ANNOTATED("ANNOTATED");
    private final String value;
}
