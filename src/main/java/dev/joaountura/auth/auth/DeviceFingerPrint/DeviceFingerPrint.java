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

    @Column(unique = true)
    private String fingerPrint;

    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="user_id", unique = true)
    private Users users;

}
