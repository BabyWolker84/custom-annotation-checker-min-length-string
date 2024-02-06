package rustbuilds.server.annotation.validators.minLength;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;

import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public class MinLengthValidator implements ConstraintValidator<MinLength, String> {
    private final Environment env;
    private final MessageSource messageSource;
    private String min;
    @Override
    public void initialize(MinLength constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
    @Override
    public boolean isValid(String valueStr, ConstraintValidatorContext constraintValidatorContext) {
        Locale locale = LocaleContextHolder.getLocale();
        if (min.isEmpty()) {
            String message = messageSource.getMessage(
                    "custom_annotation.min_length.message.parameter.min.is_empty", null, locale);
            log.error(message);
            throw new RuntimeException();
        }
        if (valueStr == null || valueStr.isEmpty()) {
            String message = messageSource.getMessage(
                    "custom_annotation.length.message.value.no_value", null, locale);
            log.warn(message);
            return true;
        }
        String minValueObj = env.getProperty(min);
        if (minValueObj == null || minValueObj.isEmpty()) {
            String message = messageSource.getMessage(
                    "custom_annotation.min_length.message.min.value.not_set", null, locale);
            log.error(message);
            throw new RuntimeException();
        }
        int minValue = Integer.parseInt(minValueObj);
        if (valueStr.length() < minValue) {
            String message = constraintValidatorContext.getDefaultConstraintMessageTemplate();
            message = messageSource.getMessage(message, null, locale);
            message = message.formatted(minValue);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            log.warn(message);
            return false;
        }
        return true;
    }
}
