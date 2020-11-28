package ua.ugolek.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailService
{
    private JavaMailSender mailSender;

    @Value("${client.admin.url}")
    private String clientAdminUrl;

    @Autowired
    private ClientService clientService;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        log.info("Sent message to {} with subject {}", to, subject);
    }

    public void sendResetPasswordMessage(String to, String token) {
        log.info("Sending reset password message to {}", to);
        String url = clientAdminUrl + "/changePassword?token=" + token;
        sendMessage(to, "Forgot password", "Ссылка для восстановления пароля: " + url);
    }

    public void sendMessageToAllClients(String subject, String content) {

    }
}
