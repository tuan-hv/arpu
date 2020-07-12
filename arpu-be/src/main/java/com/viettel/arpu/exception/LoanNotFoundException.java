/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.constant.AppConstants;
import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

/**
 * Nhận vào Id khoản vay và tìm kiếm khoản vay trong database
 * Nếu không tìm thấy khoản vay đó trong database thì phải trả ra thông báo lỗi
 * response status HttpStatus.Not_Found (404).
 */

public class LoanNotFoundException extends RuntimeException implements ErrorResponseSupport {
    private static final ErrorResponse error = new ErrorResponse(AppConstants.LOAN_NOT_FOUND);

    public LoanNotFoundException() {
        super(error.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return error;
    }
}
