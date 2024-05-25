package com.demiphea.validation;

import com.demiphea.validation.NullOrNotBlank.List;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * NullOrNotBlank
 * 字符串可以为null, 如果不为null则不能为空串或空白串
 *
 * @author demiphea
 * @since 17.0.9
 */
@Documented
@Constraint(validatedBy = {NullOrNotBlankValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface NullOrNotBlank {
    String message() default "{com.demiphea.validation.NullOrNotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        NullOrNotBlank[] value();
    }
}
