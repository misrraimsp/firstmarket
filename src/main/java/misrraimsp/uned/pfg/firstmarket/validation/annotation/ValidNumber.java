package misrraimsp.uned.pfg.firstmarket.validation.annotation;

import misrraimsp.uned.pfg.firstmarket.validation.ValidNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidNumberValidator.class)
@Documented
public @interface ValidNumber {
    String message() default "{validation.numeric.generic}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String name();
}
