package dev.joaountura.auth.auth.Cookies;



import dev.joaountura.auth.auth.Tokens.JwtToken.JWTComponent;
import dev.joaountura.auth.auth.Tokens.RefreshToken.RefreshTokenComponent;
import dev.joaountura.auth.auth.Tokens.TwoFA.TwoFAServices;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieComponent {

    @Value("${app.enviroment}")
    private String enviroment;

    @Autowired
    private JWTComponent jwtComponent;

    @Autowired
    private TwoFAServices twoFAServices;

    @Autowired
    private RefreshTokenComponent refreshTokenComponent;

    public final static String cookieJWTName = "jwtToken";
    public final static String cookieRefreshName = "refreshToken";
    public final static String cookieTwoFaName = "twoFaJwtToken";

    public Cookie generateJWTCookie(String jwtToken){

        Cookie cookie = new Cookie(cookieJWTName, jwtToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtComponent.getDuration());
        cookie.setSecure(this.enviroment != "dev");

        return cookie;
    }

    public Cookie generateTwoFaCookie(String twoFaJwtToken){
        Cookie cookie = new Cookie(cookieTwoFaName, twoFaJwtToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(twoFAServices.getDuration());
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
