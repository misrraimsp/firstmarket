package misrraimsp.uned.pfg.firstmarket.util.event.listener;

import misrraimsp.uned.pfg.firstmarket.core.model.User;
import misrraimsp.uned.pfg.firstmarket.core.service.UserServer;
import misrraimsp.uned.pfg.firstmarket.util.event.OnResetPasswordEvent;
import misrraimsp.uned.pfg.firstmarket.util.mail.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    private final MailClient mailClient;
    private final UserServer userServer;
    private final MailProperties mailProperties;

    @Autowired
    public ResetPasswordListener(MailClient mailClient,
                                 UserServer userServer,
                                 MailProperties mailProperties){

        this.mailClient = mailClient;
        this.userServer = userServer;
        this.mailProperties = mailProperties;
    }

    public void onApplicationEvent(OnResetPasswordEvent onResetPasswordEvent) {
        User user = userServer.findById(onResetPasswordEvent.getUserId());
        Map<String,Object> properties = new HashMap<>();
        properties.put("user", user);
        properties.put("contactAddress", mailProperties.getUsername());
        properties.put("pw", onResetPasswordEvent.getRandomPassword());
        mailClient.prepareAndSend("mail/pwReset",properties,user.getEmail(),"Reset Password");
    }
}
