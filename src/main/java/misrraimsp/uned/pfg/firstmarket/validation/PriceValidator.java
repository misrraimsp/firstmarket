package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidPrice;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceValidator implements ConstraintValidator<ValidPrice, String> {

    private ValidationRegexProperties validationRegexProperties;

    private Pattern pattern;
    private Matcher matcher;

    @Autowired
    public PriceValidator(ValidationRegexProperties validationRegexProperties) {
        this.validationRegexProperties = validationRegexProperties;
    }

    @Override
    public void initialize(ValidPrice constraintAnnotation) { }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        pattern = Pattern.compile(validationRegexProperties.getPrice());
        matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
