package com.epam.esm.service.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserCredentialsDto {

    @NotBlank(message = "login must not be empty")
    @Pattern(regexp = "[A-Za-z0-9_.]{4,256}", message = "login can contain only latin characters, underscores and " +
            "dots. Length must be between 3 and 256 characters")
    private String login;

    @NotBlank(message = "password must not be empty")
    @Pattern(regexp = "[A-Za-zА-Яа-яЁё0-9!?@#$%^&*()\\-_+:;,.]{6,256}", message = "password can contain latin and " +
            "cyrillic characters, punctuation marks and special characters '@#$%^&*()'. Length must be between " +
            "6 and 256 characters")
    private String password;
}
