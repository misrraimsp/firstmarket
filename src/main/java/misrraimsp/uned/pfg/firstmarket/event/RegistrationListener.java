package misrraimsp.uned.pfg.firstmarket.event;

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
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserServer userServer;
    private MailServer mailServer;
    private MessageSource messageSource;

    @Autowired
    public RegistrationListener(UserServer userServer, MailServer mailServer, MessageSource messageSource){

        this.userServer = userServer;
        this.mailServer = mailServer;
        this.messageSource = messageSource;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {

        User user = onRegistrationCompleteEvent.getUser();
        VerificationToken verificationToken = userServer.createVerificationToken(user);

        MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("FirstMarket Confirm Registration");

        String text = messageSource.getMessage("email.confirm", null, null);
        text += "<a href='" + onRegistrationCompleteEvent.getAppUrl() + "/registrationConfirm?token=" + verificationToken.getToken() + "'>here</a>";
        mailMessage.setText(text);

        mailServer.send(mailMessage);
    }
}
