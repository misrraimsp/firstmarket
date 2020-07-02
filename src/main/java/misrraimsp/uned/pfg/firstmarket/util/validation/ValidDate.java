package misrraimsp.uned.pfg.firstmarket.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateValidator.class)
public @interface ValidDate {

    String message() default "{date.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
