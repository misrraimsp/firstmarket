package misrraimsp.uned.pfg.firstmarket.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StockValidator.class)
@Documented
public @interface ValidStock {
    String message() default "numeric range error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
