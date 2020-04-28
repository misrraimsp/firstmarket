package misrraimsp.uned.pfg.firstmarket.validation.annotation;

import misrraimsp.uned.pfg.firstmarket.validation.ValidPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPatternValidator.class)
@Documented
public @interface ValidPattern {
    String message() default "{validation.regex.generic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String pattern();
}
