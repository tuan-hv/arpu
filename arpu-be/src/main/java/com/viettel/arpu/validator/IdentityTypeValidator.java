/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @Author tuongvx
 */
public class IdentityTypeValidator implements ConstraintValidator<IdentityType, String> {
    @Override
    public void initialize(IdentityType constraintAnnotation) {
        //Do nothing
    }

    @Override
    public boolean isValid(String identityType, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(identityType)) {
            return false;
        }
        return Objects.equals(identityType.toLowerCase(), "cmnd")
                              || Objects.equals(identityType.toLowerCase(), "cccd");
    }
}
