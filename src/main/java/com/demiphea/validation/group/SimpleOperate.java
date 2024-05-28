package com.demiphea.validation.group;

import jakarta.validation.groups.Default;

/**
 * SimpleOperate
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface SimpleOperate extends Default {
    interface Insert extends SimpleOperate {
    }

    interface Update extends SimpleOperate {
    }
}
