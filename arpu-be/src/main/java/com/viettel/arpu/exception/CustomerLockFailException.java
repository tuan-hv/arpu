/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.CUSTOMER_VERSION_INVALID;

/**
 * @author trungnb3
 * @Date :5/21/2020, Thu
 * throw khi khóa cho vay đối với customer xảy ra lỗi
 */
public class CustomerLockFailException extends RuntimeException implements ErrorResponseSupport {
    public CustomerLockFailException() {
        super(CUSTOMER_VERSION_INVALID.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return CUSTOMER_VERSION_INVALID;
    }
}
