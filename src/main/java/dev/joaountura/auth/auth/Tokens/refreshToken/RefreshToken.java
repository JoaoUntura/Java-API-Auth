package dev.joaountura.auth.auth.Tokens.refreshToken;

import dev.joaountura.auth.user.models.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private boolean active = true;

    @Column(unique = true)
    private String token;


    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();


    @Builder.Default
    private Instant expires_at = Instant.now().plusSeconds(86400 * 15);

    @OneToOne
    @JoinColumn(name="user_id", unique = true)
    private Users users;



}
