package misrraimsp.uned.pfg.firstmarket.event;

import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.context.ApplicationEvent;


public class OnEmailConfirmationNeededEvent extends ApplicationEvent {

    private User user;
    private String editedEmail;

    public OnEmailConfirmationNeededEvent(User user, String editedEmail) {
        super(user);
        this.user = user;
        this.editedEmail = editedEmail;
    }

    public User getUser(){
        return user;
    }

    public String getEditedEmail(){
        return editedEmail;
    }
}
