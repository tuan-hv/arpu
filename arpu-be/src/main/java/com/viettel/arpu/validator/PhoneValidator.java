/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @Author VuHQ
 * @Since 6/2/2020
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final Pattern phoneRegex = Pattern.compile("^[0-9]*$");
    private static final int PHONE_LENGTH = 12;
    @Override
    public void initialize(Phone constraintAnnotation) {
        //Do nothing
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return true;
        }
        return phoneRegex.matcher(phoneNumber).matches() && phoneNumber.length() <= PHONE_LENGTH;
    }
}
