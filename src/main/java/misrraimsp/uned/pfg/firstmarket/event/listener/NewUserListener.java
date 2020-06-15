package misrraimsp.uned.pfg.firstmarket.event.listener;

import misrraimsp.uned.pfg.firstmarket.event.OnNewUserEvent;
import misrraimsp.uned.pfg.firstmarket.mail.MailClient;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NewUserListener implements ApplicationListener<OnNewUserEvent> {

    private final MailClient mailClient;
    private final UserServer userServer;
    private final MailProperties mailProperties;

    @Autowired
    public NewUserListener(MailClient mailClient,
                           UserServer userServer,
                           MailProperties mailProperties){

        this.mailClient = mailClient;
        this.userServer = userServer;
        this.mailProperties = mailProperties;
    }

    public void onApplicationEvent(OnNewUserEvent onNewUserEvent) {
        User user = userServer.findById(onNewUserEvent.getUserId());
        Map<String,Object> properties = new HashMap<>();
        properties.put("user", user);
        properties.put("contactAddress",mailProperties.getUsername());
        mailClient.prepareAndSend("mail/accountNew",properties,user.getEmail(),"Welcome to FirstMarket");
    }
}
