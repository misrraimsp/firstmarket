package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) { }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context){
        User user = (User) object;
        return user.getPassword().equals(user.getMatchingPassword());
    }

}
