package misrraimsp.uned.pfg.firstmarket.util.event;

import misrraimsp.uned.pfg.firstmarket.core.model.User;
import org.springframework.context.ApplicationEvent;

public class OnPaymentSuccessEvent extends ApplicationEvent {

    private final User user;

    public OnPaymentSuccessEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
