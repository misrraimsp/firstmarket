package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.adt.dto.MatchingPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) { }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context){
        MatchingPassword matchingPassword = (MatchingPassword) object;
        return matchingPassword.getPassword().equals(matchingPassword.getMatchingPassword());
    }

}
