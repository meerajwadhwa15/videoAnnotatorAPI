package com.videoannotator.model.request;

import com.videoannotator.validation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@PasswordMatches
@Schema(description = "If it's change password action need to oldPassword")
public class PasswordRequest {
    private String oldPassword;
    @NotBlank
    private String password;
    @NotBlank
    private String matchingPassword;
}
