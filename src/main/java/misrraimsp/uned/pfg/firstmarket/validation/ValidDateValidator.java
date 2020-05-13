package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.adt.dto.DateContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDate;

public class ValidDateValidator implements ConstraintValidator<ValidDate, DateContainer> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void initialize(ValidDate constraintAnnotation) { }

    @Override
    public boolean isValid(DateContainer dateContainer, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate validDate = LocalDate.of(dateContainer.getYear(), dateContainer.getMonth(), dateContainer.getDay());
            LOGGER.debug("Date({}) successfully validated", validDate);
            return true;
        }
        catch (DateTimeException e) {
            LOGGER.debug("Date(y={}, m={}, d={}) invalid", dateContainer.getYear(), dateContainer.getMonth(), dateContainer.getDay());
            return false;
        }
    }
}
