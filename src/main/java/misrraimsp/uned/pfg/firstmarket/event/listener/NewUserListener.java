package misrraimsp.uned.pfg.firstmarket.event.listener;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.event.OnNewUserEvent;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class NewUserListener implements ApplicationListener<OnNewUserEvent> {

    private MailServer mailServer;
    private UserServer userServer;

    @Autowired
    public NewUserListener(MailServer mailServer, UserServer userServer){
        this.mailServer = mailServer;
        this.userServer = userServer;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnNewUserEvent onNewUserEvent) {
        User user = userServer.findById(onNewUserEvent.getUserId());
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
