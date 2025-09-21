package dev.joaountura.auth.user.mappers;

import dev.joaountura.auth.user.models.Users;
import dev.joaountura.auth.user.models.UserCreateDTO;
import dev.joaountura.auth.user.models.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users toUser(UserCreateDTO userCreateDTO){
        return  Users.builder()
                .email(userCreateDTO.getEmail())
                .password(passwordEncoder.encode(userCreateDTO.getPassword()))
                .build();
    }

    public List<UserResponseDTO> toUserResponse(List<Users> users){

        return users.stream().map(user -> (UserResponseDTO.builder()
                .email(user.getEmail())
                .dateCreated(user.getDateCreated())
                .externalId(user.getExternalId())
                .role(user.getRole())
                .build())).toList();
    }

}
