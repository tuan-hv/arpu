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
 * @Since 6/18/2020
 */
public class OtpValidator implements ConstraintValidator<Otp, String> {
    private static final Pattern otpRegex = Pattern.compile("^[0-9]*$");
    private static final int OTP_LENGTH = 6;
    @Override
    public void initialize(Otp constraintAnnotation) {
        //No thing
    }

    @Override
    public boolean isValid(String otp, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(otp)) {
            return true;
        }
        return otpRegex.matcher(otp).matches() && otp.length() == OTP_LENGTH;
    }
}
