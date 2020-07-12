/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.REASON;

/**
 * Khi từ chối khoản vay, bắt buộc phải truyền vào lý do từ chối
 * không truyền sẽ hiển thị ra thông báo lỗi
 * response status HttpStatus.BAD_REQUEST (400).
 */

public class ReasonMissingException extends RuntimeException implements ErrorResponseSupport {
    public ReasonMissingException() {
        super(REASON.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return REASON;
    }
}
