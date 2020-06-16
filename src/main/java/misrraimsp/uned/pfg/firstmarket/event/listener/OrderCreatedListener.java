package misrraimsp.uned.pfg.firstmarket.event.listener;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.HostProperties;
import misrraimsp.uned.pfg.firstmarket.event.OnOrderCreatedEvent;
import misrraimsp.uned.pfg.firstmarket.mail.MailClient;
import misrraimsp.uned.pfg.firstmarket.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderCreatedListener implements ApplicationListener<OnOrderCreatedEvent> {

    private final MailClient mailClient;
    private final MailProperties mailProperties;
    private final HostProperties hostProperties;

    @Autowired
    public OrderCreatedListener(MailClient mailClient,
                                MailProperties mailProperties,
                                HostProperties hostProperties) {

        this.mailClient = mailClient;
        this.mailProperties = mailProperties;
        this.hostProperties = hostProperties;
    }

    @Override
    public void onApplicationEvent(OnOrderCreatedEvent onOrderCreatedEvent) {
        Order order = onOrderCreatedEvent.getOrder();
        Map<String,Object> properties = new HashMap<>();
        properties.put("order", order);
        properties.put("user", order.getUser());
        properties.put("contactAddress",mailProperties.getUsername());
        properties.put("linkAddress", hostProperties.getAddress() + "orders");
        mailClient.prepareAndSend("mail/orderNew",properties,order.getUser().getEmail(),"FirstMarket New Order");
    }
}
