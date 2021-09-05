package com.videoannotator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN(1),
    NORMAL(2),
    CONSUMER(3);
    private final long value;
}
