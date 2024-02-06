package rustbuilds.server.annotation.validators.minLength;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MinLengthValidator.class)
public @interface MinLength {
    String message() default "";
    String min() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
