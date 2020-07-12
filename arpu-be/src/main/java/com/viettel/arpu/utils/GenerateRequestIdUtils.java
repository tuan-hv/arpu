/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author VuHQ
 * @Since 6/19/2020
 */
public class GenerateRequestIdUtils {

    public static String generateRequestId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterRequestId = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
        return now.format(formatterRequestId);
    }

    private GenerateRequestIdUtils() {
    }
}
