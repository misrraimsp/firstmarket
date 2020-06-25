package misrraimsp.uned.pfg.firstmarket.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {

    String message() default "{password.notMatching}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
