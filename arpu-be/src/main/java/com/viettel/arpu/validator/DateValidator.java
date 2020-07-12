/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;

/**
 * @author tuongvx
 */
@Slf4j
public class DateValidator implements ConstraintValidator<Date, String> {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(Date constraintAnnotation) {
        //Do nothing
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }
        boolean valid = true;
        try {
            formatter.parse(date);
        } catch (Exception e) {
            log.error("Error validate: " + e);
            valid = false;
        }
        return valid;
    }
}
