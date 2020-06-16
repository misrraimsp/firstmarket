package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationNumericProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class ValidNumberValidator implements ConstraintValidator<ValidNumber, Integer> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final ValidationNumericProperties validationNumericProperties;
    private String fieldName;

    @Autowired
    public ValidNumberValidator(ValidationNumericProperties validationNumericProperties){
        this.validationNumericProperties = validationNumericProperties;
    }

    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        fieldName = constraintAnnotation.name();
    }

    @Override
    public boolean isValid(Integer number, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field field = ValidationNumericProperties.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (number >= ((int[]) field.get(validationNumericProperties))[0] &&
                    number <= ((int[]) field.get(validationNumericProperties))[1]
            );
        }
        catch (NoSuchFieldException e) {
            LOGGER.warn("No such field={} on {} class. Exception: {}", fieldName, validationNumericProperties.getClass(), e);
            return true;
        }
        catch (IllegalAccessException e) {
            LOGGER.error("Illegal access to field={} on {} class. Exception: {}", fieldName, validationNumericProperties.getClass(), e);
            return false;
        }

    }
}
