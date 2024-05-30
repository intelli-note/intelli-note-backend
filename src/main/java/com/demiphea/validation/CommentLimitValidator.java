package com.demiphea.validation;

import com.demiphea.model.dto.comment.CommentDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * CommentLimitValidator
 *
 * @author demiphea
 * @since 17.0.9
 */
public class CommentLimitValidator implements ConstraintValidator<CommentLimit, CommentDto> {
    @Override
    public boolean isValid(CommentDto value, ConstraintValidatorContext context) {
        int count = 0;
        if (value.getImageList() != null) {
            count++;
        }
        if (value.getAudio() != null) {
            if (++count > 1) {
                return false;
            }
        }
        if (value.getVideo() != null) {
            if (++count > 1) {
                return false;
            }
        }
        if (value.getLinkNoteId() != null) {
            if (++count > 1) {
                return false;
            }
        }
        if (count == 0) {
            return value.getText() != null;
        }
        return true;
    }
}
