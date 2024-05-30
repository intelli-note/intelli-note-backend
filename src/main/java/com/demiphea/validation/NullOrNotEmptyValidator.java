package com.demiphea.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

/**
 * NullOrNotEmptyValidator
 *
 * @author demiphea
 * @since 17.0.9
 */
public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, Collection> {
    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.isEmpty();
    }
}
