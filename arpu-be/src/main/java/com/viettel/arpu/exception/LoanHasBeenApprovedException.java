/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.LOAN_HAS_BEEN_APPROVED;

/**
 * Nếu thực hiện action phê duyệt hoặc từ chối với 1 khoản vay đã được phê duyệt hoặc từ chối
 * Thì sẽ trả ra thông báo lỗi
 * response status HttpStatus.BAD_REQUEST (400).
 */

public class LoanHasBeenApprovedException extends RuntimeException implements ErrorResponseSupport {
    public LoanHasBeenApprovedException() {
        super(LOAN_HAS_BEEN_APPROVED.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return LOAN_HAS_BEEN_APPROVED;
    }
}
