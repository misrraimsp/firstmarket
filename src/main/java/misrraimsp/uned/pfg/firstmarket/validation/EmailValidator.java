package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.Patterns;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail,String>, Patterns {

    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(ValidEmail constraintAnnotation) { }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return (validateEmail(email));
    }

    private boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
