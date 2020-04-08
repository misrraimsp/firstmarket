package misrraimsp.uned.pfg.firstmarket.event.listener;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.event.OnEmailConfirmationNeededEvent;
import misrraimsp.uned.pfg.firstmarket.event.security.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.model.SecurityToken;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmationNeededListener implements ApplicationListener<OnEmailConfirmationNeededEvent> {

    private UserServer userServer;
    private MailServer mailServer;
    private MessageSource messageSource;

    @Autowired
    public EmailConfirmationNeededListener(UserServer userServer, MailServer mailServer, MessageSource messageSource){
        this.userServer = userServer;
        this.mailServer = mailServer;
        this.messageSource = messageSource;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnEmailConfirmationNeededEvent onEmailConfirmationNeededEvent) {
        // create a security token
        SecurityEvent securityEvent = onEmailConfirmationNeededEvent.getSecurityEvent();
        User user = userServer.findById(onEmailConfirmationNeededEvent.getUserId());
        String editedEmail = onEmailConfirmationNeededEvent.getEditedEmail();
        SecurityToken securityToken = userServer.createSecurityToken(securityEvent, user, editedEmail);

        // Build the email message
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("FirstMarket Confirm Email");
        String text = "";
        text += messageSource.getMessage("email.confirm", null, null);
        text += "<a href='http://localhost:8080/firstmarket/emailConfirmation?token=" + securityToken.getToken() + "'>Confirm Email</a>";
        mailMessage.setText(text);
        if (securityEvent.equals(SecurityEvent.EMAIL_CHANGE)) { // change email process
            mailMessage.setTo(editedEmail);
        }
        else { // new user registration or forgot password
            mailMessage.setTo(user.getEmail());
        }

        // send email
        mailServer.send(mailMessage);
    }

}
