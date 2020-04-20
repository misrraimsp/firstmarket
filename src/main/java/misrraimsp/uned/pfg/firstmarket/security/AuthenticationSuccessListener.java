package misrraimsp.uned.pfg.firstmarket.security;

import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private LockManager lockManager;

    @Autowired
    public AuthenticationSuccessListener(LockManager lockManager) {
        this.lockManager = lockManager;
    }

    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();
        lockManager.loginSuccess(user.getUsername());
    }
}
