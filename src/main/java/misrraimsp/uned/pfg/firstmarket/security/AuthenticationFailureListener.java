package misrraimsp.uned.pfg.firstmarket.security;

import lombok.SneakyThrows;
import misrraimsp.uned.pfg.firstmarket.adt.MailMessage;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityLockProperties;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.MailServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private LockManager lockManager;
    private UserServer userServer;
    private MailServer mailServer;
    private SecurityLockProperties securityLockProperties;

    @Autowired
    public AuthenticationFailureListener(LockManager lockManager,
                                         UserServer userServer,
                                         MailServer mailServer,
                                         SecurityLockProperties securityLockProperties) {

        this.lockManager = lockManager;
        this.userServer = userServer;
        this.mailServer = mailServer;
        this.securityLockProperties = securityLockProperties;
    }

    @SneakyThrows
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = (String) event.getAuthentication().getPrincipal();
        if (userServer.emailExists(email)){
            // cache failure
            lockManager.loginFail(email);
            // send email if just locked
            if (lockManager.isLocked(email)) {
                User user = userServer.getUserByEmail(email);
                // Build the email message
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Timestamp(calendar.getTime().getTime()));
                calendar.add(Calendar.MINUTE, securityLockProperties.getLockingMinutes());
                MailMessage mailMessage = new MailMessage();
                mailMessage.setSubject("Account Locked");
                String text = "<h1>Dear " + user.getProfile().getFirstName() + ",</h1>";
                text += "<p>Due to several login failures, your FirstMarket account has been locked for security reasons</p>";
                text += "<p>The account will remain locked until:</p>";
                text += "<p><strong>" + calendar.getTime().toString() + "</strong></p>";
                mailMessage.setText(text);
                mailMessage.setTo(email);
                // send email
                mailServer.send(mailMessage);
            }
        }
    }

}
