package dev.joaountura.auth.login.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private final LocalDateTime localDateTime = LocalDateTime.now();
}
