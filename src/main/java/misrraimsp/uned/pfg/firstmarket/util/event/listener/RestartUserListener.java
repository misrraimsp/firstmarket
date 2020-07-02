package misrraimsp.uned.pfg.firstmarket.event.listener;

import misrraimsp.uned.pfg.firstmarket.event.OnRestartUserEvent;
import misrraimsp.uned.pfg.firstmarket.mail.MailClient;
import misrraimsp.uned.pfg.firstmarket.core.model.User;
import misrraimsp.uned.pfg.firstmarket.core.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RestartUserListener implements ApplicationListener<OnRestartUserEvent> {

    private final MailClient mailClient;
    private final UserServer userServer;
    private final MailProperties mailProperties;

    @Autowired
    public RestartUserListener(MailClient mailClient,
                               UserServer userServer,
                               MailProperties mailProperties){

        this.mailClient = mailClient;
        this.userServer = userServer;
        this.mailProperties = mailProperties;
    }

    public void onApplicationEvent(OnRestartUserEvent onRestartUserEvent) {
        User user = userServer.findById(onRestartUserEvent.getUserId());
        Map<String,Object> properties = new HashMap<>();
        properties.put("user", user);
        properties.put("contactAddress",mailProperties.getUsername());
        mailClient.prepareAndSend("mail/accountRestored",properties,user.getEmail(),"Account Restoration");
    }
}
