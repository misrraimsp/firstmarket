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
public class EmailEditionListener  implements ApplicationListener<OnEmailEditionEvent> {

    private MailServer mailServer;
    //private MessageSource messageSource;

    @Autowired
    public EmailEditionListener(MailServer mailServer
                                    //, MessageSource messageSource
    ){
        this.mailServer = mailServer;
        //this.messageSource = messageSource;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnEmailEditionEvent onEmailEditionEvent) {
        User user = onEmailEditionEvent.getUser();
        // Build the email message
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("Email Successfully Changed");
        String text = "<h1>Hi " + user.getProfile().getFirstName() + "!</h1>";
        text += "<p>Your email address has been successfully modified</p>";
        mailMessage.setText(text);
        mailMessage.setTo(user.getEmail());
        // send email
        mailServer.send(mailMessage);
    }
}
