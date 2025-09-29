package dev.joaountura.auth.email.services;

import dev.joaountura.auth.user.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {

    @Autowired
    private JavaMailSender javaMailSender;

    private final String mailFrom = "joaountura@gmail.com";

    public void send2FAEmail(int code, Users user){

        String message = "Verifique sua Identidade\nInsira este código para finalizar e completar seu login\n Código de Verificação\n" +
                code;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail().strip());
        simpleMailMessage.setSubject("Verifique sua Identidade");
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom(this.mailFrom);

        javaMailSender.send(simpleMailMessage);

    }

   public void sendNewDeviceEmail(Users user){
       String message = "Atenção! Novo dispositivo acabou de fazer Login em sua conta";

       SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
       simpleMailMessage.setTo(user.getEmail().strip());
       simpleMailMessage.setSubject("Novo dispositivo");
       simpleMailMessage.setText(message);
       simpleMailMessage.setFrom(this.mailFrom);

       javaMailSender.send(simpleMailMessage);

   }

}
