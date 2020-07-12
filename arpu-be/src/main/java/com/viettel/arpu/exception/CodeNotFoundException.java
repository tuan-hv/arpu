/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.CODE_NOT_FOUND;
/**
 *
 * @author DoDV * @Date :6/5/2020, Fri
 * Trả ra thông báo lỗi khi truyền vào code id không tìm thấy trong database
 */

public class CodeNotFoundException extends RuntimeException implements ErrorResponseSupport {

    public CodeNotFoundException() {
        super(CODE_NOT_FOUND.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return CODE_NOT_FOUND;
    }
}
