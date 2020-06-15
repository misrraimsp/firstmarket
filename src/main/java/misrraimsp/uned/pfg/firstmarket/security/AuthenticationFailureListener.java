package misrraimsp.uned.pfg.firstmarket.security;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityLockProperties;
import misrraimsp.uned.pfg.firstmarket.mail.MailClient;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final LockManager lockManager;
    private final UserServer userServer;
    private final MailClient mailClient;
    private final SecurityLockProperties securityLockProperties;
    private final MailProperties mailProperties;

    @Autowired
    public AuthenticationFailureListener(LockManager lockManager,
                                         UserServer userServer,
                                         MailClient mailClient,
                                         SecurityLockProperties securityLockProperties,
                                         MailProperties mailProperties) {

        this.lockManager = lockManager;
        this.userServer = userServer;
        this.mailClient = mailClient;
        this.securityLockProperties = securityLockProperties;
        this.mailProperties = mailProperties;
    }

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = (String) event.getAuthentication().getPrincipal();
        LOGGER.debug("Authentication failure (email account: {})", email);
        if (userServer.emailExists(email)){
            // cache failure
            lockManager.loginFail(email);
            if (lockManager.isLocked(email)) { // send email if just locked (it is just locked because the event is BadCredential. During the following logins the authentication failure will be Locked)
                LOGGER.debug("Account locked (email: {})", email);
                User user = userServer.getUserByEmail(email);
                Map<String,Object> properties = new HashMap<>();
                properties.put("user", user);
                properties.put("contactAddress",mailProperties.getUsername());
                properties.put("lockTime", securityLockProperties.getLockingMinutes());
                mailClient.prepareAndSend("mail/accountLocked",properties,user.getEmail(),"Account Locked");
            }
        }
    }

}
