package dev.joaountura.auth.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO {

    private String email;
    private UUID externalId;
    private String dateCreated;
    private Roles role;

}
