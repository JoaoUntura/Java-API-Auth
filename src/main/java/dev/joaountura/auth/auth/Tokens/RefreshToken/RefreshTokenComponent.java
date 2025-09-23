package dev.joaountura.auth.auth.Tokens.RefreshToken;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.auth.Tokens.TokenAbstract;
import dev.joaountura.auth.exceptions.refreshTokenExceptions.InvalidUserException;
import dev.joaountura.auth.exceptions.refreshTokenExceptions.RefreshTokenExpiredException;
import dev.joaountura.auth.exceptions.refreshTokenExceptions.RefreshTokenInactiveException;
import dev.joaountura.auth.exceptions.refreshTokenExceptions.RefreshTokenNotFoundException;
import dev.joaountura.auth.user.models.Users;
import dev.joaountura.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenComponent extends TokenAbstract {

    @Autowired
    private RefreshRepository refreshRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshTokenComponent(@Value("refresh.secret") String refreshSecret)
    {
        this.duration = 86400 * 15;
        this.secret = refreshSecret;
    }

    public RefreshToken createRefresh(Users users){
        Optional<RefreshToken> refreshTokenExistent =  refreshRepository.findByUsers(users);
        refreshTokenExistent.ifPresent(refreshToken -> refreshRepository.delete(refreshToken));

        String token = super.encodeToken(users);
        RefreshToken refreshToken = RefreshToken.builder()
                .users(users)
                .token(token)
                .build();
        return refreshRepository.save(refreshToken);

    }

    public Users verifyRefreshTokenAndGetUser(String token) throws Exception {

        DecodedJWT decodedJWT = super.verifyAndDecode(token);

        UUID userUUID = UUID.fromString(decodedJWT.getSubject());

        Users user = userRepository.findByExternalId(userUUID);
        if (user == null) throw new InvalidUserException();

        RefreshToken refreshToken = refreshRepository.findByToken(decodedJWT.getToken())
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (!refreshToken.isActive()) throw new RefreshTokenInactiveException();
        if (Instant.now().isAfter(refreshToken.getExpires_at())) throw new RefreshTokenExpiredException();
        if (!Objects.equals(refreshToken.getUsers().getId(), user.getId())) throw new InvalidUserException();

        return user;

    }

}
