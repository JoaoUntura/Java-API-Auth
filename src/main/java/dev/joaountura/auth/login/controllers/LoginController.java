package dev.joaountura.auth.login.controllers;
import dev.joaountura.auth.auth.Cookies.CookieComponent;
import dev.joaountura.auth.auth.DeviceFingerPrint.DeviceFingerPrintServices;
import dev.joaountura.auth.login.models.Login2FADTO;
import dev.joaountura.auth.login.models.LoginDTO;
import dev.joaountura.auth.login.models.LoginResponseDTO;
import dev.joaountura.auth.login.services.LoginAttemptService;
import dev.joaountura.auth.login.services.LoginServices;
import dev.joaountura.auth.user.models.Users;
import dev.joaountura.auth.user.services.UserServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginServices loginServices;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private CookieComponent cookieComponent;


    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String fingerPrint = loginServices.generateFingerPrint(request);

        loginAttemptService.checkAttemptsByFingerPrint(fingerPrint);

        Users user = loginServices.loginUserValidationService(loginDTO);

        loginServices.deviceFingerPrintValidation(fingerPrint, user);

        loginServices.sendTwoFaCode(user);
        String twoFaJwtToken = loginServices.generate2faToken(user);
        Cookie twoFaCookie = cookieComponent.generateTwoFaCookie(twoFaJwtToken);
        response.addCookie(twoFaCookie);


        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO("2fa Code send to email " + loginDTO.getEmail()));
    }

    @PostMapping("/2fa")
    public ResponseEntity<LoginResponseDTO> login2fa(@RequestBody Login2FADTO login2FADTO, HttpServletResponse response, @AuthenticationPrincipal String userUUID) throws Exception {

        Users user = loginServices.validateTwoFaAndReturnUser(login2FADTO.getCode(), userUUID);

        String jwtToken = loginServices.generateUserJWTToken(user);
        String refreshToken = loginServices.generateUserRefreshToken(user);
        Cookie cookieJwt = cookieComponent.generateJWTCookie(jwtToken);
        Cookie cookieRefresh = cookieComponent.generateRefreshTokenCookie(refreshToken);

        response.addCookie(cookieJwt);
        response.addCookie(cookieRefresh);


        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO("Success"));
    }

}
