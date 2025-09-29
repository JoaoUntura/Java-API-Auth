package dev.joaountura.auth.auth.DeviceFingerPrint;

import dev.joaountura.auth.user.models.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceFingerPrint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fingerPrint;

    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;

}
