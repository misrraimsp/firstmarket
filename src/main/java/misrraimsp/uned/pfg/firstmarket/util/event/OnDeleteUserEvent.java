package misrraimsp.uned.pfg.firstmarket.util.event;

import misrraimsp.uned.pfg.firstmarket.core.model.UserDeletion;
import org.springframework.context.ApplicationEvent;

public class OnDeleteUserEvent extends ApplicationEvent {

    private final UserDeletion userDeletion;

    public OnDeleteUserEvent(UserDeletion userDeletion) {
        super(userDeletion);
        this.userDeletion = userDeletion;
    }

    public UserDeletion getUserDeletion(){
        return userDeletion;
    }
}
