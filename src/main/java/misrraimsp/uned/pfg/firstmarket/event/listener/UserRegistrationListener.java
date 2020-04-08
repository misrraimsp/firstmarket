package misrraimsp.uned.pfg.firstmarket.event.listener;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.event.OnUserRegistrationEvent;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
//import org.springframework.context.MessageSource;

@Component
public class UserRegistrationListener implements ApplicationListener<OnUserRegistrationEvent> {

    private MailServer mailServer;
    private UserServer userServer;
    //private MessageSource messageSource;

    @Autowired
    public UserRegistrationListener(MailServer mailServer, UserServer userServer
            //, MessageSource messageSource
    ){
        this.mailServer = mailServer;
        this.userServer = userServer;
        //this.messageSource = messageSource;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnUserRegistrationEvent onUserRegistrationEvent) {
        User user = userServer.findById(onUserRegistrationEvent.getUserId());
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
