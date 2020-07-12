/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant;

public class MessageCode {
    protected String code;
    protected String message;

    public MessageCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageCode(String code) {
        this(code, "");
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
