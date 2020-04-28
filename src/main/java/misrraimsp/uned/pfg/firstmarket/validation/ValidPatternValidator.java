package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
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
        if (text == null) return true;
        try {
            Field regexField = ValidationRegexProperties.class.getDeclaredField(fieldName);
            regexField.setAccessible(true);
            String regex = (String) regexField.get(validationRegexProperties);
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            boolean isValid = matcher.matches();
            LOGGER.debug("String({}) validation with patternName({}) (regex={}) has been {}", text, fieldName, regex, isValid);
            return isValid;
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
