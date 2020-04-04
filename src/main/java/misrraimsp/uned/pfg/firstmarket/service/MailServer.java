package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailServer {

    private Properties properties;

    private static final String senderAddress = "afirstmarket@gmail.com";
    private static final String senderPassword = "expedienteXD3";

    public MailServer(){
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }


    public void send(MailMessage mailMessage) throws MessagingException {

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
        message.setContent(mailMessage.getText(), "text/html");

        Transport.send(message);
    }
}
