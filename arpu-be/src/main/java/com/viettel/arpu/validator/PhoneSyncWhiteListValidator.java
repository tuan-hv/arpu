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
 * @Author tuongvx
 * @Since 6/24/2020
 */
public class PhoneSyncWhiteListValidator implements ConstraintValidator<PhoneSyncWhiteList, String> {
    private static final Pattern phoneRegex = Pattern.compile("^[0-9]*$");
    private static final int PHONE_MIN_LENGTH = 10;
    private static final int PHONE_MAX_LENGTH = 11;

    @Override
    public void initialize(PhoneSyncWhiteList constraintAnnotation) {
        //Do nothing
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        return phoneRegex.matcher(phoneNumber).matches()
               && phoneNumber.length() <= PHONE_MAX_LENGTH && phoneNumber.length() >= PHONE_MIN_LENGTH;
    }
}
