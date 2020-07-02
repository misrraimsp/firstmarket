package misrraimsp.uned.pfg.firstmarket.event;

import misrraimsp.uned.pfg.firstmarket.core.model.Order;
import org.springframework.context.ApplicationEvent;

public class OnOrderCreatedEvent extends ApplicationEvent {

    private final Order order;

    public OnOrderCreatedEvent(Order order) {
        super(order);
        this.order = order;
    }

    public Order getOrder(){
        return order;
    }
}
