package misrraimsp.uned.pfg.firstmarket.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.OrderStatus;
import misrraimsp.uned.pfg.firstmarket.exception.NoEntityStatusException;
import misrraimsp.uned.pfg.firstmarket.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public OrderStatus convert(String s) {
        if (OrderStatus.values().length == 0) throw new NoEntityStatusException(Order.class.getSimpleName());
        try {
            int index = Integer.parseInt(s);
            int maxIndex = OrderStatus.values().length - 1;
            if (index > maxIndex) {
                LOGGER.warn("OrderStatus index({}) out-of-bound({}). Defaults to {}", index, maxIndex, OrderStatus.PROCESSING);
                return OrderStatus.PROCESSING;
            }
            return OrderStatus.values()[index];
        }
        catch (NumberFormatException e) {
            LOGGER.error("OrderStatus index string({}) can not be converted due to format exception. Defaults to {}", s, OrderStatus.PROCESSING, e);
            return OrderStatus.PROCESSING;
        }
    }
}
