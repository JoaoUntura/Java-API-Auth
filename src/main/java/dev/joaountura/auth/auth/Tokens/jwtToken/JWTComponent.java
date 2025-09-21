package dev.joaountura.auth.auth.Tokens.jwtToken;


import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.auth.Tokens.TokenAbstract;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JWTComponent extends TokenAbstract {

    public JWTComponent() {
        this.duration = 3600;
    }

    public void verifyExpiration(DecodedJWT decodedJWT) throws ServletException {
        if(Instant.now().isAfter(decodedJWT.getExpiresAtAsInstant())){
            throw new ServletException("Expired JWT Token");
        }
    }

}
