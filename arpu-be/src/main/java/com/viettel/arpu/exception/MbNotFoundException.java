/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.locale.Translator;

/**
 * Trả ra lỗi khi không tìm thấy MB
 */

public class MbNotFoundException extends RuntimeException {
    public MbNotFoundException(String message) {
        super(Translator.toLocale(message));
    }
}
