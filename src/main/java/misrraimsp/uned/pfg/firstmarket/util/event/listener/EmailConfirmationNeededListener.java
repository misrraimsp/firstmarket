package misrraimsp.uned.pfg.firstmarket.util.event.listener;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.HostProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.core.model.SecurityToken;
import misrraimsp.uned.pfg.firstmarket.core.model.User;
import misrraimsp.uned.pfg.firstmarket.core.service.UserServer;
import misrraimsp.uned.pfg.firstmarket.util.event.OnEmailConfirmationNeededEvent;
import misrraimsp.uned.pfg.firstmarket.util.mail.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailConfirmationNeededListener implements ApplicationListener<OnEmailConfirmationNeededEvent> {

    private final UserServer userServer;
    private final MailClient mailClient;
    private final MailProperties mailProperties;
    private final HostProperties hostProperties;

    @Autowired
    public EmailConfirmationNeededListener(UserServer userServer,
                                           MailClient mailClient,
                                           MailProperties mailProperties,
                                           HostProperties hostProperties){

        this.userServer = userServer;
        this.mailClient = mailClient;
        this.mailProperties = mailProperties;
        this.hostProperties = hostProperties;
    }

    public void onApplicationEvent(OnEmailConfirmationNeededEvent onEmailConfirmationNeededEvent) {
        // create a security token
        SecurityEvent securityEvent = onEmailConfirmationNeededEvent.getSecurityEvent();
        User user = userServer.findById(onEmailConfirmationNeededEvent.getUserId());
        String editedEmail = onEmailConfirmationNeededEvent.getEditedEmail();
        SecurityToken securityToken = userServer.createSecurityToken(securityEvent, user, editedEmail);

        // Build and send email message
        String recipient = (securityEvent.equals(SecurityEvent.EMAIL_CHANGE)) ? editedEmail : user.getEmail();
        Map<String,Object> properties = new HashMap<>();
        properties.put("user", user);
        properties.put("contactAddress",mailProperties.getUsername());
        properties.put("linkAddress", hostProperties.getAddress() + "emailConfirmation?token=" + securityToken.getToken());
        mailClient.prepareAndSend("mail/confirmEmail",properties,recipient,"Confirm Email");
    }

}
