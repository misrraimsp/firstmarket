package misrraimsp.uned.pfg.firstmarket.util.event;

import org.springframework.context.ApplicationEvent;

public class OnRestartUserEvent extends ApplicationEvent {

    private final Long userId;

    public OnRestartUserEvent(Long userId) {
        super(userId);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
