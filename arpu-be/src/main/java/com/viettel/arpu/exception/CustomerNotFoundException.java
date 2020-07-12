/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.exception;

import com.viettel.arpu.model.response.ErrorResponse;
import com.viettel.arpu.model.response.ErrorResponseSupport;

import static com.viettel.arpu.constant.ErrorConstant.CUSTOMER_NOT_FOUND;

/**
 * @author trungnb3
 * @Date :5/21/2020, Thu
 * throw khi không tìm thấy customer
 */
public class CustomerNotFoundException extends RuntimeException implements ErrorResponseSupport {
    public CustomerNotFoundException() {
        super(CUSTOMER_NOT_FOUND.getMessage());
    }

    @Override
    public ErrorResponse toErrorResponse() {
        return CUSTOMER_NOT_FOUND;
    }
}
