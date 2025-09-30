package dev.joaountura.auth.auth.filters;


import dev.joaountura.auth.auth.Cookies.CookieComponent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class FilterServices {

    public Cookie findCookie(HttpServletRequest request, String cookieName) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {

            return null;
        }

        Optional<Cookie> cookie = Arrays.stream(cookies)
                .filter(cookie1 -> Objects.equals(cookie1.getName(), cookieName)).findFirst();

        return cookie.orElse(null);

    }

}
