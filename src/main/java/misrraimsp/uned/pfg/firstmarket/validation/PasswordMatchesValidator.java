package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.adt.dto.TwinPasswordContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, TwinPasswordContainer> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void initialize(PasswordMatches constraintAnnotation) { }

    @Override
    public boolean isValid(TwinPasswordContainer twinPasswordContainer, ConstraintValidatorContext context){
        boolean isValid = twinPasswordContainer.getPassword().equals(twinPasswordContainer.getMatchingPassword());
        LOGGER.debug("Validating password matching condition: {}", isValid);
        return isValid;
    }

}
