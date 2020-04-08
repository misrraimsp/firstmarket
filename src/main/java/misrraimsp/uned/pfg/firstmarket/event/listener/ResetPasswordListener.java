package misrraimsp.uned.pfg.firstmarket.event.listener;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.event.OnResetPasswordEvent;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
//import org.springframework.context.MessageSource;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    private MailServer mailServer;
    private UserServer userServer;
    //private MessageSource messageSource;

    @Autowired
    public ResetPasswordListener(MailServer mailServer, UserServer userServer
                                    //, MessageSource messageSource
    ){
        this.mailServer = mailServer;
        this.userServer = userServer;
        //this.messageSource = messageSource;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnResetPasswordEvent onResetPasswordEvent) {
        User user = userServer.findById(onResetPasswordEvent.getUserId());
        String randomPassword = onResetPasswordEvent.getRandomPassword();
        // Build the email message
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject("Reset Password");
        String text = "<h1>Hi " + user.getProfile().getFirstName() + "!</h1>";
        text += "<p>This is your reset password: <strong>" + randomPassword + "</strong></p>";
        text += "<p>Please, don't forget to change it.</p>";
        mailMessage.setText(text);
        mailMessage.setTo(user.getEmail());
        // send email
        mailServer.send(mailMessage);
    }
}
