package dev.joaountura.auth.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.auth.Cookies.CookieComponent;
import dev.joaountura.auth.auth.Tokens.JwtToken.JWTComponent;
import dev.joaountura.auth.auth.Tokens.RefreshToken.RefreshTokenComponent;
import dev.joaountura.auth.login.models.LoginResponseDTO;
import dev.joaountura.auth.user.models.Users;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<LoginResponseDTO> refresh(@AuthenticationPrincipal DecodedJWT decodedJWT, HttpServletResponse response) throws Exception {
       Users relatedUser =  refreshTokenComponent.verifyRefreshTokenAndGetUser(decodedJWT);
       String newJwtToken = jwtComponent.encodeToken(relatedUser);

       Cookie cookie1 = cookieComponent.generateJWTCookie(newJwtToken);
        response.addCookie(cookie1);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO("New Jwt Created"));


    }

}
