package dev.joaountura.auth.auth.Tokens.JwtToken;


import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.auth.Tokens.TokenAbstract;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JWTComponent extends TokenAbstract {

    public JWTComponent( @Value("${jwt.secret}") String jwtSecret) {
        this.duration = 3600;

        this.secret = jwtSecret;

    }

    public void verifyExpiration(DecodedJWT decodedJWT) throws ServletException {
        if(Instant.now().isAfter(decodedJWT.getExpiresAtAsInstant())){
            throw new ServletException("Expired JWT Token");
        }
    }

}
