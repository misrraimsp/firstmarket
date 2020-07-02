package misrraimsp.uned.pfg.firstmarket.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort.OrderSortCriteria;
import misrraimsp.uned.pfg.firstmarket.exception.NoSortCriteriaException;
import misrraimsp.uned.pfg.firstmarket.core.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderSortCriteriaConverter implements Converter<String, OrderSortCriteria> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public OrderSortCriteria convert(String s) {
        if (OrderSortCriteria.values().length == 0) throw new NoSortCriteriaException(Order.class.getSimpleName());
        try {
            int index = Integer.parseInt(s);
            int maxIndex = OrderSortCriteria.values().length - 1;
            if (index > maxIndex) {
                LOGGER.warn("OrderSortCriteria index({}) out-of-bound({}). Defaults to {}", index, maxIndex, OrderSortCriteria.CREATED_DATE_DESC);
                return OrderSortCriteria.CREATED_DATE_DESC;
            }
            return OrderSortCriteria.values()[index];
        }
        catch (NumberFormatException e) {
            LOGGER.error("OrderSortCriteria index string({}) can not be converted due to format exception. Defaults to {}", s, OrderSortCriteria.CREATED_DATE_DESC, e);
            return OrderSortCriteria.CREATED_DATE_DESC;
        }
    }
}
