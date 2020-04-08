package misrraimsp.uned.pfg.firstmarket.event;

import misrraimsp.uned.pfg.firstmarket.event.security.SecurityEvent;
import org.springframework.context.ApplicationEvent;


public class OnEmailConfirmationNeededEvent extends ApplicationEvent {

    private SecurityEvent securityEvent;
    private Long userId;
    private String editedEmail;

    public OnEmailConfirmationNeededEvent(SecurityEvent securityEvent, Long userId, String editedEmail) {
        super(userId);
        this.securityEvent = securityEvent;
        this.userId = userId;
        this.editedEmail = editedEmail;
    }

    public SecurityEvent getSecurityEvent(){
        return securityEvent;
    }

    public Long getUserId(){
        return userId;
    }

    public String getEditedEmail(){
        return editedEmail;
    }
}
