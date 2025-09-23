package dev.joaountura.auth.login.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.auth.DeviceFingerPrint.DeviceFingerPrintServices;
import dev.joaountura.auth.auth.Tokens.JwtToken.JWTComponent;
import dev.joaountura.auth.auth.Tokens.RefreshToken.RefreshTokenComponent;
import dev.joaountura.auth.auth.Tokens.TwoFA.TwoFAServices;
import dev.joaountura.auth.login.models.LoginDTO;
import dev.joaountura.auth.user.models.Users;
import dev.joaountura.auth.user.services.UserServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTComponent jwtComponent;

    @Autowired
    private DeviceFingerPrintServices deviceFingerPrintServices;

    @Autowired
    private RefreshTokenComponent refreshTokenComponent;

    @Autowired
    private TwoFAServices twoFAServices;

    @Autowired
    private UserServices userServices;

    public Users loginUserValidationService(LoginDTO loginDTO){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        authenticationManager.authenticate(authenticationToken);
        return userServices.findUserByEmail(loginDTO.getEmail());
    }

    public String generateUserJWTToken(Users user){

        return jwtComponent.encodeToken(user);
    }

    public String generateUserRefreshToken(Users user){

        return refreshTokenComponent.createRefresh(user).getToken();
    }

    public String generate2faToken(Users user){
        return twoFAServices.createTwoFaJWT(user);
    }


    public void deviceFingerPrintValidation(HttpServletRequest request, Users user){
        String fingerPrint = deviceFingerPrintServices.generateFingerPrint(request);
        deviceFingerPrintServices.findFingerPrintOrSaveNew(fingerPrint, user);
    }

    public void sendTwoFaCode(Users users){
        twoFAServices.sendTwoFaCode(users);
    }

    public Users validateTwoFaAndReturnUser(String code, String twoFaCookie) throws Exception {
        DecodedJWT decodedJWT = twoFAServices.verifyAndDecode(twoFaCookie);
        Users user = userServices.findUserByUUID(UUID.fromString(decodedJWT.getSubject()));
        if (user == null) throw new ServletException("User not Found");
        twoFAServices.compareTwoFaCodes(user, code);
        return user;
    }

}
