/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.constant;

import lombok.NoArgsConstructor;

/**
 * @author trungnb3
 * @Date :6/3/2020, Wed
 */
public class AppConstants {

    private static final String ARPU400 = "ARPU400";
    private static final String ERROR_MSG_OTP_FAIL = "error.msg.otp.fail";
    private AppConstants() {
    }
    //System code
    public static final MessageCode OK = new MessageCode("00", "00");
    public static final MessageCode OTP_FAIL = new MessageCode("32", ERROR_MSG_OTP_FAIL);
    public static final MessageCode OTP_FAIL_IN_FINAL_LOAN = new MessageCode("324", ERROR_MSG_OTP_FAIL);
    public static final MessageCode OTP_FAIL_IN_VIET_TEL= new MessageCode("AUT0008", ERROR_MSG_OTP_FAIL);
    public static final MessageCode NOT_FOUND = new MessageCode("ARPU404", "msg.not_found");
    public static final MessageCode SUCCESS = new MessageCode("ARPU200", "msg.success");
    public static final MessageCode NO_CONTENT = new MessageCode("ARPU204", "msg.success");
    public static final MessageCode INTERNAL_SERVER_ERROR = new MessageCode("ARPU500", "error.msg.internal_server");
    public static final MessageCode BAD_REQUEST = new MessageCode(ARPU400, "msg.bad_request");
    public static final MessageCode CONNECTION_TIMEOUT = new MessageCode("ARPU504", "msg.connection_timeout");
    public static final MessageCode VALIDATE_MB_FAIL =  new MessageCode("ARPU452", "msg.validate.mb.fail");
    public static final MessageCode VALIDATE_OTP_FAIL = new MessageCode("ARPU453", ERROR_MSG_OTP_FAIL);
    //response for customer
    public static final MessageCode MSG_30 = new MessageCode("ARPU030", "msg_30");
    public static final MessageCode MSG_32 = new MessageCode("ARPU032", "msg_32");
    public static final MessageCode MSG_23 = new MessageCode("ARPU023", "msg_23");
    //response for loan
    public static final MessageCode LOAN_NOT_FOUND = new MessageCode("ARPU405", "msg.loan_not_found");

    //response for file
    public static final MessageCode FILE_NOT_FOUND = new MessageCode(ARPU400, "msg.file_not_found");

    public static final MessageCode FILE_INVALID = new MessageCode(ARPU400, "msg.file_invalid");

    //validator
    public static final MessageCode TYPE_MISMATCH = new MessageCode("error.msg.request.param.type.mismatch");
    public static final MessageCode MISSING_SERVLET_REQUEST_PARAMETER =
                    new MessageCode("error.msg.request.param.require");

}
