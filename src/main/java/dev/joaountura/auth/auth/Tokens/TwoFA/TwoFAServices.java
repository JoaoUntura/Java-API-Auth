package dev.joaountura.auth.auth.Tokens.TwoFA;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.joaountura.auth.auth.Tokens.TokenAbstract;
import dev.joaountura.auth.user.models.Users;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
public class TwoFAServices extends TokenAbstract {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public TwoFAServices(@Value("2fa.secret") String twoJwtSecret) {
        this.duration = 300;
        this.secret = twoJwtSecret;
    }

    public String createTwoFaJWT(Users users){
        return super.encodeToken(users);
    }

    public void sendTwoFaCode(Users users){
        int code = generateCode();

        System.out.println(code);//envia email

        storeTwoFaRedis(hashCode(code), users.getId());

    }

    public void compareTwoFaCodes(Users users, String userTwoFaAtempt) throws Exception {

        TwoFA twoFA = getTwoFA(users);

        if (twoFA == null) throw new ServletException("2fa not Found");

        if(Instant.now().isAfter(twoFA.getExpiresAt())) throw new ServletException("Expired 2fa");

        boolean matches = passwordEncoder.matches(userTwoFaAtempt, twoFA.getHashedCode());

        if (!matches){
            twoFA.setAttempts(twoFA.getAttempts() + 1);
            updateTwoFaRedis(twoFA);
            throw new ServletException("Incorret 2fa Code");
        }

    }

    private void updateTwoFaRedis(TwoFA twoFA){
        redisTemplate.opsForValue().set(String.valueOf(twoFA.getUserId()), twoFA);
    }

    private void storeTwoFaRedis(String hashedToken, Integer userId){
        TwoFA twoFA = TwoFA.builder()
                .hashedCode(hashedToken)
                .userId(userId)
                .build();

        redisTemplate.opsForValue().set(userId.toString(), twoFA, Duration.ofMinutes(5));
    }

    private TwoFA getTwoFA(Users users){

        Object obj = redisTemplate.opsForValue().get(users.getId().toString());
        return objectMapper.convertValue(obj, TwoFA.class);


    }

    private int generateCode(){
        SecureRandom random = new SecureRandom();
         return random.nextInt(900000) + 100000;
    }


    private String hashCode(int code) {
        return passwordEncoder.encode(String.valueOf(code));
    }
}
