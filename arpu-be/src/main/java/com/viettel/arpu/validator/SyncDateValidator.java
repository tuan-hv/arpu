/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.arpu.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author tuongvx
 */
@Slf4j
public class SyncDateValidator implements ConstraintValidator<SyncDate, String> {
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");

    @Override
    public void initialize(SyncDate constraintAnnotation) {
        //Do nothing
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }
        boolean valid = true;
        try {
            DateTime dateTime = DateTime.parse(date, formatter);
            valid = dateTime.compareTo(DateTime.now()) <= 0;
        } catch (Exception e) {
            log.error("Error validate: " + e);
            valid = false;
        }
        return valid;
    }
}
