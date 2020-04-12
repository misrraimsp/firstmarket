package misrraimsp.uned.pfg.firstmarket.event.listener;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.event.OnRestartUserEvent;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RestartUserListener implements ApplicationListener<OnRestartUserEvent> {

    private MailServer mailServer;
    private UserServer userServer;

    @Autowired
    public RestartUserListener(MailServer mailServer, UserServer userServer){
        this.mailServer = mailServer;
        this.userServer = userServer;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnRestartUserEvent onRestartUserEvent) {
        User user = userServer.findById(onRestartUserEvent.getUserId());
        // Build the email message
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("Account Restoration");
        String text = "<h1>Hi " + user.getProfile().getFirstName() + "!</h1>";
        text += "<p>You successfully restart your FirstMarket account. All of your account settings has been restored.</p>";
        text += "<p>If you do not remember your password, please click <a href='http://localhost:8080/firstmarket/resetPassword'>here</a></p>";
        mailMessage.setText(text);
        mailMessage.setTo(user.getEmail());
        // send email
        mailServer.send(mailMessage);
    }
}
