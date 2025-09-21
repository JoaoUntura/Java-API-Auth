package dev.joaountura.auth.login.services;

import dev.joaountura.auth.auth.Tokens.jwtToken.JWTComponent;
import dev.joaountura.auth.auth.Tokens.refreshToken.RefreshToken;
import dev.joaountura.auth.auth.Tokens.refreshToken.RefreshTokenComponent;
import dev.joaountura.auth.login.models.LoginDTO;
import dev.joaountura.auth.user.models.Users;
import dev.joaountura.auth.user.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginServices {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JWTComponent jwtComponent;


    @Autowired
    private RefreshTokenComponent refreshTokenComponent;

    public void loginValidationService(LoginDTO loginDTO){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        authenticationManager.authenticate(authenticationToken);
    }

    public String generateUserJWTToken(Users user){

        return jwtComponent.encodeToken(user);
    }

    public String generateUserRefreshToken(Users user){

        return refreshTokenComponent.createRefresh(user).getToken();
    }

}
