package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationNumericProperties;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidNumPages;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumPagesValidator implements ConstraintValidator<ValidNumPages, Integer> {

    private ValidationNumericProperties validationNumericProperties;

    @Autowired
    public NumPagesValidator(ValidationNumericProperties validationNumericProperties){

        this.validationNumericProperties = validationNumericProperties;
    }

    @Override
    public void initialize(ValidNumPages constraintAnnotation) { }

    @Override
    public boolean isValid(Integer numPages, ConstraintValidatorContext constraintValidatorContext) {
        return (numPages <= validationNumericProperties.getPagesMax() &&
                numPages >= validationNumericProperties.getPagesMin()
        );
    }
}
