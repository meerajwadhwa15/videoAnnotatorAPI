package com.videoannotator.model.request;

import com.videoannotator.constant.RoleEnum;
import com.videoannotator.validation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@PasswordMatches
@Accessors(chain = true)
public class RegisterRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String matchingPassword;
    @Schema(description = "1=Admin; 2=Normal; 3=Consumer")
    private Long userType = RoleEnum.NORMAL.getValue();
}
