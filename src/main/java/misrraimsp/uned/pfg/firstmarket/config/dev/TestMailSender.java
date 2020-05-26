package misrraimsp.uned.pfg.firstmarket.config.dev;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class TestMailSender {

    public static void main(String[] args) {
        System.out.println("Start sending mail");

        try {
            sendMail();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        System.out.println("Mail sent");
    }

    private static void sendMail() throws MessagingException {

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String senderAddress = "afirstmarket@gmail.com";
        String senderPassword = "#";

        String recipientAddress = "suarezperezmisrraim@gmail.com";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderAddress, senderPassword);
            }
        });

        Message message = buildMessage(session, senderAddress, recipientAddress);

        Transport.send(message);

    }

    private static Message buildMessage(Session session, String senderAddress, String recipientAddress) {

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderAddress));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
            message.setSubject("My first email from java app");
            //message.setText("Hola cuchi, \n GUAPA!");
            String htmlContent = "<a href='http://localhost:8080/firstmarket/home'>Visit my web app here!</a>";
            message.setContent(htmlContent, "text/html");
            return message;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
