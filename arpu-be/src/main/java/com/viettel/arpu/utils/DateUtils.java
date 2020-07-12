/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.utils;

import com.viettel.arpu.exception.DateIncorrectException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author VuHQ
 * @Since 6/3/2020
 */
public class DateUtils {

    private DateUtils() {
    }

    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static LocalDate convertInstantToLocalDate(Instant instant){
        return instant.atZone(ZONE_ID).toLocalDate();
    }

    public static LocalDateTime convertInstantToLocalDateTime(Instant instant){
        return instant.atZone(ZONE_ID).toLocalDateTime();
    }

    public static Date convertStringToDate(String date){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException parseException) {
            throw new DateIncorrectException();
        }
    }
}
