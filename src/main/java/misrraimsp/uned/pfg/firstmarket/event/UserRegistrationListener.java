package misrraimsp.uned.pfg.firstmarket.event;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
//import org.springframework.context.MessageSource;

@Component
public class UserRegistrationListener implements ApplicationListener<OnUserRegistrationEvent> {

    private MailServer mailServer;
    //private MessageSource messageSource;

    @Autowired
    public UserRegistrationListener(MailServer mailServer
            //, MessageSource messageSource
    ){
        this.mailServer = mailServer;
        //this.messageSource = messageSource;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnUserRegistrationEvent onUserRegistrationEvent) {
        User user = onUserRegistrationEvent.getUser();
        // Build the email message
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("Welcome to FirstMarket");
        String text = "<h1>Hi " + user.getProfile().getFirstName() + "!</h1>";
        text += "<p>Welcome to <strong>FirstMarket</strong></p>";
        mailMessage.setText(text);
        mailMessage.setTo(user.getEmail());
        // send email
        mailServer.send(mailMessage);
    }
}
