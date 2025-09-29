package dev.joaountura.auth.login.services;

import dev.joaountura.auth.exceptions.authExceptions.TooManyRequests;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginAttemptService {

    private final Duration loginWindow = Duration.ofMinutes(5);

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public void checkAttemptsByFingerPrint(String fingerPrint) throws TooManyRequests {
        Long attempts = redisTemplate.opsForValue().increment(fingerPrint);

        if (attempts == 1) {
            redisTemplate.expire(fingerPrint, loginWindow);
        }

        System.out.println("Attempts: " + attempts);

        if (attempts > 3) {
            throw new TooManyRequests();
        }
    }

    public void loginSucceeded(String fingerPrint) {
        redisTemplate.delete(fingerPrint);
    }
}
