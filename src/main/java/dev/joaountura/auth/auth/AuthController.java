package dev.joaountura.auth.auth;

import dev.joaountura.auth.auth.Cookies.CookieComponent;
import dev.joaountura.auth.auth.Tokens.JwtToken.JWTComponent;
import dev.joaountura.auth.auth.Tokens.RefreshToken.RefreshTokenComponent;
import dev.joaountura.auth.user.models.Users;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/refresh")
public class AuthController {

    @Autowired
    private RefreshTokenComponent refreshTokenComponent;

    @Autowired
    private CookieComponent cookieComponent;

    @Autowired
    private JWTComponent jwtComponent;

    @GetMapping
    public ResponseEntity<String> refresh(@CookieValue(name = CookieComponent.cookieRefreshName, required = true) Cookie cookie, HttpServletResponse response) throws Exception {
       Users relatedUser =  refreshTokenComponent.verifyRefreshTokenAndGetUser(cookie.getValue());
       String newJwtToken = jwtComponent.encodeToken(relatedUser);

       Cookie cookie1 = cookieComponent.generateJWTCookie(newJwtToken);
        response.addCookie(cookie1);

        return ResponseEntity.status(HttpStatus.OK).body("New JWT created");


    }

}
