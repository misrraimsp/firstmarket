package misrraimsp.uned.pfg.firstmarket.event;

import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private User user;

    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}