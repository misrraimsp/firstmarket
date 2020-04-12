package misrraimsp.uned.pfg.firstmarket.event;

import misrraimsp.uned.pfg.firstmarket.model.UserDeletion;
import org.springframework.context.ApplicationEvent;

public class OnDeleteUserEvent extends ApplicationEvent {

    private UserDeletion userDeletion;

    public OnDeleteUserEvent(UserDeletion userDeletion) {
        super(userDeletion);
        this.userDeletion = userDeletion;
    }

    public UserDeletion getUserDeletion(){
        return userDeletion;
    }
}
