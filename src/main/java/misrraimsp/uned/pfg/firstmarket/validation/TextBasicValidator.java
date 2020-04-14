package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextBasicValidator implements ConstraintValidator<ValidTextBasic, String> {

    private ValidationRegexProperties validationRegexProperties;

    private Pattern pattern;
    private Matcher matcher;

    @Autowired
    public TextBasicValidator(ValidationRegexProperties validationRegexProperties) {
        this.validationRegexProperties = validationRegexProperties;
    }

    @Override
    public void initialize(ValidTextBasic constraintAnnotation) { }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        pattern = Pattern.compile(validationRegexProperties.getTextBasic());
        matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
