/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.model.response.ErrorResponse;

/**
 * Trả ra một thông báo lỗi khi kết quả trả về từ MB bị lỗi
 */
public class MbResponse extends ErrorResponse {

    private MbResponse(String code, String message) {
        super(code, message, null);
    }

    public static MbResponse from(String code, String message) {
        return new MbResponse(code, message);
    }

    public void check() {
        if (AppConstants.OTP_FAIL.getCode().equalsIgnoreCase(getCode())
                || AppConstants.OTP_FAIL_IN_FINAL_LOAN.getCode().equalsIgnoreCase(getCode())
                || AppConstants.OTP_FAIL_IN_VIET_TEL.getCode().equalsIgnoreCase(getCode())) {
            throw new MbOtpFailException();
        }
        if (!AppConstants.OK.getCode().equalsIgnoreCase(getCode())) {
            throw new MbResponseException(getCode(), this.message);
        }
    }
}
