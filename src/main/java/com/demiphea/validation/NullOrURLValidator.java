package com.demiphea.validation;

import cn.hutool.core.lang.Validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * NullOrURLValidator
 *
 * @author demiphea
 * @since 17.0.9
 */
public class NullOrURLValidator implements ConstraintValidator<NullOrURL, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Validator.isUrl(value);
    }
}
