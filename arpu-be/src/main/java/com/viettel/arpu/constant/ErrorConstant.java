/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant;

import com.viettel.arpu.model.response.ErrorResponse;

public class ErrorConstant {
    private static final String ARPU400 = "ARPU400";
    public static final ErrorResponse CODE_NOT_FOUND = new ErrorResponse("ARPU406", "msg.code_not_found");
    public static final ErrorResponse CUSTOMER_VERSION_INVALID =
            new ErrorResponse("ARPU301", "msg.customer_version_invalid");
    public static final ErrorResponse CUSTOMER_NOT_FOUND =
            new ErrorResponse("ARPU404", "error.msg.whitelist.id_not_found");
    public static final ErrorResponse CUSTOMER_HAS_BEEN_LOCK =
            new ErrorResponse("ARPU407", "msg.customer_has_been_lock");
    public static final ErrorResponse CUSTOMER_HAS_BEEN_UNLOCK =
            new ErrorResponse("ARPU408", "msg.customer_has_been_unlock");

    public static final ErrorResponse LOAN_HAS_BEEN_APPROVED = new ErrorResponse(ARPU400, "msg.loan_has_been_approved");
    public static final ErrorResponse CODE_INPUT_INVALID = new ErrorResponse(ARPU400, "msg.code_input_invalid");
    public static final ErrorResponse LOAN_STATUS_INVALID = new ErrorResponse(ARPU400, "msg.loan_status_invalid");
    public static final ErrorResponse ID_INVALID = new ErrorResponse(ARPU400, "msg.id_invalid");
    public static final ErrorResponse REASON = new ErrorResponse(ARPU400, "msg.reason_missing");
    public static final ErrorResponse DATA_INPUT_INVALID = new ErrorResponse(ARPU400, "msg.data_input_invalid");
    private ErrorConstant() {
    }
}
