package dev.joaountura.auth.user.models;

import lombok.Data;

@Data
public class UserCreateDTO{
    private String email;
    private String password;
}
