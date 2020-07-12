/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.validator;

import com.viettel.arpu.constant.BatchConstant;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class SexValidator implements ConstraintValidator<Sex, String> {

    @Override
    public void initialize(Sex constraintAnnotation) {
        //Do nothing
    }

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(gender)) {
            return false;
        }
        return Objects.equals(BatchConstant.MALE, gender.toUpperCase())
                              || Objects.equals(BatchConstant.FEMALE, gender.toUpperCase());
    }
}
