package misrraimsp.uned.pfg.firstmarket.validation.annotation;

import misrraimsp.uned.pfg.firstmarket.validation.TextLongValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextLongValidator.class)
@Documented
public @interface ValidTextLong {
    String message() default "text-long error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
