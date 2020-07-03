package misrraimsp.uned.pfg.firstmarket.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPatternValidator.class)
@Documented
public @interface ValidPattern {
    String message() default "{validation.regex.generic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String pattern();
}
