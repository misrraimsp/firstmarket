package misrraimsp.uned.pfg.firstmarket.event.listener;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.event.OnDeleteUserEvent;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserListener implements ApplicationListener<OnDeleteUserEvent> {

    private MailServer mailServer;

    @Autowired
    public DeleteUserListener(MailServer mailServer){
        this.mailServer = mailServer;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnDeleteUserEvent onDeleteUserEvent) {
        User user = onDeleteUserEvent.getUserDeletion().getUser();
        // Build the email message
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("Account Deleted");
        String text = "<h1>Hi " + user.getProfile().getFirstName() + "!</h1>";
        text += "<p>You successfully deleted your FirstMarket account</p>";
        mailMessage.setText(text);
        mailMessage.setTo(user.getEmail());
        // send email
        mailServer.send(mailMessage);
    }
}
