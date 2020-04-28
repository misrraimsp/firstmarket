package misrraimsp.uned.pfg.firstmarket.validation.annotation;

import misrraimsp.uned.pfg.firstmarket.validation.IsbnValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsbnValidator.class)
@Documented
public @interface ValidIsbn {
    String message() default "{isbn.error}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
