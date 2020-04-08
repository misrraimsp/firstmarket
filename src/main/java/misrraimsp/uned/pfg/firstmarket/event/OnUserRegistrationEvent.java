package misrraimsp.uned.pfg.firstmarket.event;

import org.springframework.context.ApplicationEvent;

public class OnUserRegistrationEvent extends ApplicationEvent {

    private Long userId;

    public OnUserRegistrationEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
}
