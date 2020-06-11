package misrraimsp.uned.pfg.firstmarket.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort.UserSortCriteria;
import misrraimsp.uned.pfg.firstmarket.exception.NoSortCriteriaException;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserSortCriteriaConverter implements Converter<String, UserSortCriteria> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserSortCriteria convert(String s) {
        if (UserSortCriteria.values().length == 0) throw new NoSortCriteriaException(User.class.getSimpleName());
        try {
            int index = Integer.parseInt(s);
            int maxIndex = UserSortCriteria.values().length - 1;
            if (index > maxIndex) {
                LOGGER.warn("UserSortCriteria index({}) out-of-bound({}). Defaults to {}", index, maxIndex, UserSortCriteria.LOCKED_ASC);
                return UserSortCriteria.LOCKED_ASC;
            }
            return UserSortCriteria.values()[index];
        }
        catch (NumberFormatException e) {
            LOGGER.error("UserSortCriteria index string({}) can not be converted due to format exception. Defaults to {}", s, UserSortCriteria.LOCKED_ASC, e);
            return UserSortCriteria.LOCKED_ASC;
        }
    }
}
