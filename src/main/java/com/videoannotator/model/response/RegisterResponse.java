package com.videoannotator.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterResponse<T>{
    private T id;
    public RegisterResponse() {
        //None parameter contractor
    }
}
