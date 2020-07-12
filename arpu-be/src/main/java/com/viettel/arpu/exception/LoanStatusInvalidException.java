/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.LOAN_STATUS_INVALID;

/**
 * Thực hiện gửi duyệt đối với những khoản vay ở trạng thái đang chờ MB phê duyệt
 * khi thực hiện action với những khoản vay chưa chuyển trạng thái
 * sẽ có thông báo lỗi xảy ra
 * response status HttpStatus.BAD_REQUEST (400).
 */

public class LoanStatusInvalidException extends RuntimeException implements ErrorResponseSupport {
    public LoanStatusInvalidException() {
        super(LOAN_STATUS_INVALID.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return LOAN_STATUS_INVALID;
    }
}
