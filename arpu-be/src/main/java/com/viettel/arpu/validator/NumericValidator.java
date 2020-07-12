/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NumericValidator implements ConstraintValidator<Numeric, String> {
    private static final Pattern patternNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public void initialize(Numeric constraintAnnotation) {
        //Do nothing
    }

    @Override
    public boolean isValid(String number, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        return patternNumeric.matcher(number).matches();
    }
}
