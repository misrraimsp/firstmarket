package misrraimsp.uned.pfg.firstmarket.event;

import misrraimsp.uned.pfg.firstmarket.model.Cart;
import org.springframework.context.ApplicationEvent;

public class OnCartCommittedEvent extends ApplicationEvent {

    private Cart committedCart;

    public OnCartCommittedEvent(Cart committedCart) {
        super(committedCart);
        this.committedCart = committedCart;
    }

    public Cart getCommittedCart(){
        return committedCart;
    }
}
