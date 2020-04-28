package misrraimsp.uned.pfg.firstmarket.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIsbnValidator.class)
@Documented
public @interface ValidIsbn {
    String message() default "{isbn.error}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
