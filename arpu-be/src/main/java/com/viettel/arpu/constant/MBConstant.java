/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant;

import com.viettel.arpu.exception.MbResponseException;
import com.viettel.arpu.model.response.ErrorResponse;

import java.util.function.Function;

public class MBConstant {
    public static final String SUCCESS = "00";
    public static final String KYC_APPROVAL = "1";
    public static final String KYC_REFUSE = "2";

    public static final String VERIFY_METHOD ="sms";

    public static final Function<MbResponseException, MessageCode> MB_RESPONSE = e
            -> new MessageCode("ARPU452", e.getMessage());

    public static final ErrorResponse WITHDRAW_FAIL = new ErrorResponse("ARPU454", "error.msg.mb.withdraw");
    public static final String NO_EMAIL = "noemail@gmail.com";

    public static final String DATE_IDENTIFIER = "2020-01-01";
    
    private MBConstant() {
    }
}
