package dev.joaountura.auth.auth.Tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.user.models.Users;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

abstract public class TokenAbstract {
    @Value("${jwt.issuer}")
    private  String issuer;

    protected String secret;

    @Getter
    public int duration;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(this.secret);
    }


    public String encodeToken(Users user){
        Instant expirationTime = Instant.now().plusSeconds(this.duration);
        return JWT.create()
                .withIssuer(this.issuer)
                .withSubject(user.getExternalId().toString())
                .withClaim("roles",user.getAuthorities().stream().map(Object::toString).toList())
                .withExpiresAt(expirationTime)
                .sign(this.algorithm);
    }

    public DecodedJWT verifyAndDecode(String token){
        JWTVerifier verifier = JWT.require(this.algorithm).withIssuer(this.issuer).build();

        return verifier.verify(token);

    }

}
