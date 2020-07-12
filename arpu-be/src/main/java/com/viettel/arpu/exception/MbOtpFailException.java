/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

/**
 * @Author VuHQ
 * @Since 6/23/2020
 */
public class MbOtpFailException extends RuntimeException implements ErrorResponseSupport {
    private static final ErrorResponse error = new ErrorResponse(AppConstants.VALIDATE_OTP_FAIL);

    public MbOtpFailException() {
        super(error.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return error;
    }
}
