package dev.joaountura.auth.auth.DeviceFingerPrint;
import dev.joaountura.auth.user.models.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;



@Service
public class DeviceFingerPrintServices {

    private final static Logger logger = LoggerFactory.getLogger(DeviceFingerPrintServices.class);



    @Autowired
    private DeviceFingerPrintRepository deviceFingerPrintRepository;

    public String generateFingerPrint(HttpServletRequest request) {
        String data = getUserIp(request) + safe(request.getHeader("user-agent")) + safe(request.getHeader("accept-encoding"));
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar fingerprint", e);
        }
    }

    public void findFingerPrintOrSaveNew(String fingerPrint, Users users){
        DeviceFingerPrint deviceFingerPrint = deviceFingerPrintRepository.findByFingerPrintAndUsers(fingerPrint,users);

        if (deviceFingerPrint == null){

            DeviceFingerPrint deviceFingerPrint1 = deviceFingerPrintRepository.save(
                    DeviceFingerPrint.builder()
                            .fingerPrint(fingerPrint)
                            .users(users)
                            .build()
            );
            logger.warn("New Device Login {}", deviceFingerPrint1.toString());
        }else{
            logger.info("Known Device Login {}", deviceFingerPrint.toString());
        }

    }


    private String safe(String string){
        return string == null ? "" : string.trim();
    }

    private String getUserIp(HttpServletRequest request){
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

}
