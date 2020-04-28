package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPatternValidator implements ConstraintValidator<ValidPattern, String> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ValidationRegexProperties validationRegexProperties;
    private String fieldName;

    @Autowired
    public ValidPatternValidator(ValidationRegexProperties validationRegexProperties) {
        this.validationRegexProperties = validationRegexProperties;
    }

    @Override
    public void initialize(ValidPattern constraintAnnotation) {
        fieldName = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field regexField = ValidationRegexProperties.class.getDeclaredField(fieldName);
            regexField.setAccessible(true);
            Pattern pattern = Pattern.compile((String) regexField.get(validationRegexProperties));
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        }
        catch (NoSuchFieldException e) {
            LOGGER.warn("No such field={} on {} class. Exception: {}", fieldName, validationRegexProperties.getClass(), e);
            return true;
        }
        catch (IllegalAccessException e) {
            LOGGER.error("Illegal access to field={} on {} class. Exception: {}", fieldName, validationRegexProperties.getClass(), e);
            return false;
        }
    }
}
