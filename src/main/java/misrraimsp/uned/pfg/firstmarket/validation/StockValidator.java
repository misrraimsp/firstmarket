package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationNumericProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StockValidator implements ConstraintValidator<ValidStock, Integer> {

    private ValidationNumericProperties validationNumericProperties;

    @Autowired
    public StockValidator(ValidationNumericProperties validationNumericProperties){

        this.validationNumericProperties = validationNumericProperties;
    }

    @Override
    public void initialize(ValidStock constraintAnnotation) { }

    @Override
    public boolean isValid(Integer numPages, ConstraintValidatorContext constraintValidatorContext) {
        return (numPages <= validationNumericProperties.getStockMax() &&
                numPages >= validationNumericProperties.getStockMin()
        );
    }
}
