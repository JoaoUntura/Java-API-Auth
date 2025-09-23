package dev.joaountura.auth.auth.Tokens.TwoFA;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TwoFA implements Serializable {

    private Integer userId;
    private String hashedCode;

    @Builder.Default
    private Instant expiresAt = Instant.now().plusSeconds(60 * 5);

    @Builder.Default
    private int attempts = 0;


}
