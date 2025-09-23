package dev.joaountura.auth.auth;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.joaountura.auth.auth.Cookies.CookieComponent;
import dev.joaountura.auth.auth.Tokens.JwtToken.JWTComponent;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTComponent jwtComponent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }
      Optional<Cookie> cookie = Arrays.stream(cookies)
              .filter(cookie1 -> Objects.equals(cookie1.getName(), CookieComponent.cookieJWTName)).findFirst();

      if (cookie.isEmpty()){
          filterChain.doFilter(request, response);
          return;
      }

      DecodedJWT decodedJWT = jwtComponent.verifyAndDecode(cookie.get().getValue());

      jwtComponent.verifyExpiration(decodedJWT);

        List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("roles")
                .asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        var auth = new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);
      filterChain.doFilter(request, response);


    }
}
