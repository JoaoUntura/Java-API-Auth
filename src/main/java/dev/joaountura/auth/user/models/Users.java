package dev.joaountura.auth.user.models;
import dev.joaountura.auth.auth.DeviceFingerPrint.DeviceFingerPrint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Builder.Default
    @Column(unique = true)
    private UUID externalId = UUID.randomUUID();

    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private Roles role = Roles.commom;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<DeviceFingerPrint> deviceFingerPrints;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
