package misrraimsp.uned.pfg.firstmarket.event;

import org.springframework.context.ApplicationEvent;

public class OnResetPasswordEvent extends ApplicationEvent {

    private final Long userId;
    private final String randomPassword;

    public OnResetPasswordEvent(Long userId, String randomPassword) {
        super(userId);
        this.userId = userId;
        this.randomPassword = randomPassword;
    }

    public Long getUserId(){
        return userId;
    }

    public String getRandomPassword(){
        return randomPassword;
    }
}
