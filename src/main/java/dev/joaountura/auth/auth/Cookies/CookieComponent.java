package dev.joaountura.auth.auth.Cookies;



import dev.joaountura.auth.auth.Tokens.jwtToken.JWTComponent;
import dev.joaountura.auth.auth.Tokens.refreshToken.RefreshTokenComponent;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalField;

@Component
public class CookieComponent {

    @Value("${app.enviroment}")
    private String enviroment;

    @Autowired
    private JWTComponent jwtComponent;


    @Autowired
    private RefreshTokenComponent refreshTokenComponent;

    public final static String cookieJWTName = "jwtToken";
    public final static String cookieRefreshName = "refreshToken";

    public Cookie generateJWTCookie(String jwtToken){

        Cookie cookie = new Cookie(cookieJWTName, jwtToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtComponent.getDuration());
        cookie.setSecure(this.enviroment != "dev");

        return cookie;
    }

    public Cookie generateRefreshTokenCookie(String refreshToken){

        Cookie cookie = new Cookie(cookieRefreshName, refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(refreshTokenComponent.getDuration());
        cookie.setSecure(this.enviroment != "dev");

        return cookie;
    }

}
