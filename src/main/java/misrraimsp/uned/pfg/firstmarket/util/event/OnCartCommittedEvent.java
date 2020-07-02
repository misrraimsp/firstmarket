package misrraimsp.uned.pfg.firstmarket.event;

import misrraimsp.uned.pfg.firstmarket.core.model.User;
import org.springframework.context.ApplicationEvent;

public class OnCartCommittedEvent extends ApplicationEvent {

    private final User user;

    public OnCartCommittedEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser(){
        return user;
    }

}
