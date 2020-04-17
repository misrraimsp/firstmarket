package misrraimsp.uned.pfg.firstmarket.validation.annotation;

import misrraimsp.uned.pfg.firstmarket.validation.NumPagesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumPagesValidator.class)
@Documented
public @interface ValidNumPages {
    String message() default "numeric range error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
