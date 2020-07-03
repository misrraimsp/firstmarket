package misrraimsp.uned.pfg.firstmarket.util.event;

import org.springframework.context.ApplicationEvent;

public class OnNewUserEvent extends ApplicationEvent {

    private final Long userId;

    public OnNewUserEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
}
