package misrraimsp.uned.pfg.firstmarket.event;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.model.VerificationToken;
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
        // create a verification token
        User user = onEmailConfirmationNeededEvent.getUser();
        String editedEmail = onEmailConfirmationNeededEvent.getEditedEmail();
        VerificationToken verificationToken = userServer.createVerificationToken(user, editedEmail);

        // Build the email message
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("FirstMarket Confirm Email");
        String text = "";
        text += messageSource.getMessage("email.confirm", null, null);
        text += "<a href='http://localhost:8080/firstmarket/emailConfirmation?token=" + verificationToken.getToken() + "'>Confirm Email</a>";
        mailMessage.setText(text);
        if (onEmailConfirmationNeededEvent.getEditedEmail() == null) { // new user registration process
            mailMessage.setTo(verificationToken.getUser().getEmail());
        }
        else { // change email process
            mailMessage.setTo(verificationToken.getEditedEmail());
        }

        // send email
        mailServer.send(mailMessage);
    }

}
