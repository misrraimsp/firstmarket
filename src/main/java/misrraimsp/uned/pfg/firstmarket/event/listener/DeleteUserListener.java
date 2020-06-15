package misrraimsp.uned.pfg.firstmarket.event.listener;

import misrraimsp.uned.pfg.firstmarket.event.OnDeleteUserEvent;
import misrraimsp.uned.pfg.firstmarket.mail.MailClient;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteUserListener implements ApplicationListener<OnDeleteUserEvent> {

    private final MailClient mailClient;
    private final MailProperties mailProperties;

    @Autowired
    public DeleteUserListener(MailClient mailClient, MailProperties mailProperties){
        this.mailClient = mailClient;
        this.mailProperties = mailProperties;
    }

    public void onApplicationEvent(OnDeleteUserEvent onDeleteUserEvent) {
        User user = onDeleteUserEvent.getUserDeletion().getUser();
        Map<String,Object> properties = new HashMap<>();
        properties.put("user", user);
        properties.put("contactAddress",mailProperties.getUsername());
        mailClient.prepareAndSend("mail/accountDeleted", properties , user.getEmail(), "Account Deleted");
    }
}
