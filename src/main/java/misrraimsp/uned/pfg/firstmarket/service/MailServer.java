package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailServer {

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.starttls.enable}")
    private String enable;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.address}")
    private String senderAddress;

    @Value("${mail.pw}")
    private String senderPassword;

    @Value("${mail.content-type}")
    private String contentType;

    private Properties properties;

    public MailServer(){
        properties = new Properties();
    }

    public void send(MailMessage mailMessage) throws MessagingException {

        loadProperties();
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderAddress, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderAddress));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailMessage.getTo()));
        message.setSubject(mailMessage.getSubject());
        message.setContent(mailMessage.getText(), contentType);

        Transport.send(message);
    }

    private void loadProperties() {
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", enable);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
    }
}
