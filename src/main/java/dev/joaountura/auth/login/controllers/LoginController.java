package dev.joaountura.auth.login.controllers;
import dev.joaountura.auth.auth.Cookies.CookieComponent;
import dev.joaountura.auth.login.models.LoginDTO;
import dev.joaountura.auth.login.services.LoginServices;
import dev.joaountura.auth.user.models.Users;
import dev.joaountura.auth.user.services.UserServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginServices loginServices;

    @Autowired
    private CookieComponent cookieComponent;

    @Autowired
    private UserServices userServices;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response){
        loginServices.loginValidationService(loginDTO);
        Users user = userServices.findUserByEmail(loginDTO.getEmail());
        String jwtToken = loginServices.generateUserJWTToken(user);
        String refreshToken = loginServices.generateUserRefreshToken(user);
        Cookie cookieJwt = cookieComponent.generateJWTCookie(jwtToken);
        Cookie cookieRefresh = cookieComponent.generateRefreshTokenCookie(refreshToken);

        response.addCookie(cookieJwt);
        response.addCookie(cookieRefresh);

        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }

}
