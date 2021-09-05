package com.videoannotator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewEnum {
    VERY_BAD(1),
    BAD(2),
    NORMAL(3),
    GOOD(4),
    VERY_GOOD(5);
    private final long value;
}
