package dev.joaountura.auth.auth.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.auth.Cookies.CookieComponent;
import dev.joaountura.auth.auth.Tokens.JwtToken.JWTComponent;
import dev.joaountura.auth.auth.Tokens.RefreshToken.RefreshTokenComponent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class RefreshFilter extends OncePerRequestFilter {

    @Autowired
    private RefreshTokenComponent refreshTokenComponent;

    @Autowired
    private FilterServices filterServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = filterServices.findCookie(request, CookieComponent.cookieRefreshName);

        if(cookie == null){
            filterChain.doFilter(request, response);
            return;
        }

        DecodedJWT decodedJWT = refreshTokenComponent.verifyAndDecode(cookie.getValue());

        refreshTokenComponent.verifyExpiration(decodedJWT);

        var auth = new UsernamePasswordAuthenticationToken(decodedJWT, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);


    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){

        return !request.getRequestURI().startsWith("/auth/refresh");

    }
}
