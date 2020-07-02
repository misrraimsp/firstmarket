package misrraimsp.uned.pfg.firstmarket.event;

import org.springframework.context.ApplicationEvent;

public class OnEmailEditionEvent extends ApplicationEvent {

    private final Long userId;

    public OnEmailEditionEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }

    public Long getUserId(){
        return userId;
    }
}
