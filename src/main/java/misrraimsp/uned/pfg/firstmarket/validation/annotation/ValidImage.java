package misrraimsp.uned.pfg.firstmarket.validation.annotation;

import misrraimsp.uned.pfg.firstmarket.validation.ImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
@Documented
public @interface ValidImage {
    String message() default "{image.error}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
