package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.model.FormUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) { }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context){
        FormUser formUser = (FormUser) object;
        return formUser.getPassword().equals(formUser.getMatchingPassword());
    }

}
