package misrraimsp.uned.pfg.firstmarket.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextBasicValidator.class)
@Documented
public @interface ValidTextBasic {
    String message() default "text-basic error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
