package dev.joaountura.auth.login.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginDTO {
    private String email;
    private String password;
    private final LocalDateTime localDateTime = LocalDateTime.now();
}
